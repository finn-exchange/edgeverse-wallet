package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.updaters

import com.dfinn.wallet.common.utils.staking
import com.dfinn.wallet.core.model.StorageChange
import com.dfinn.wallet.core.storage.StorageCache
import com.dfinn.wallet.core.updater.SubscriptionBuilder
import com.dfinn.wallet.core.updater.Updater
import com.dfinn.wallet.core_db.dao.AccountStakingDao
import com.dfinn.wallet.core_db.model.AccountStakingLocal
import com.dfinn.wallet.feature_account_api.domain.model.accountIdIn
import com.dfinn.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.dfinn.wallet.feature_staking_api.domain.api.StakingRepository
import com.dfinn.wallet.feature_staking_api.domain.model.StakingLedger
import com.dfinn.wallet.feature_staking_api.domain.model.isRedeemableIn
import com.dfinn.wallet.feature_staking_api.domain.model.isUnbondingIn
import com.dfinn.wallet.feature_staking_api.domain.model.sumStaking
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.data.network.blockhain.bindings.bindStakingLedger
import com.dfinn.wallet.feature_staking_impl.data.network.blockhain.updaters.base.StakingUpdater
import com.dfinn.wallet.feature_wallet_api.data.cache.AssetCache
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.getRuntime
import com.dfinn.wallet.runtime.network.updaters.insert
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.SubscribeStorageRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.storageChange
import jp.co.soramitsu.fearless_utils.wsrpc.subscriptionFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.math.BigInteger

class LedgerWithController(
    val ledger: StakingLedger,
    val controllerId: AccountId,
)

class StakingLedgerUpdater(
    private val stakingRepository: StakingRepository,
    private val stakingSharedState: StakingSharedState,
    private val chainRegistry: ChainRegistry,
    private val accountStakingDao: AccountStakingDao,
    private val storageCache: StorageCache,
    private val assetCache: AssetCache,
    override val scope: AccountUpdateScope,
) : StakingUpdater {

    override suspend fun listenForUpdates(storageSubscriptionBuilder: SubscriptionBuilder): Flow<Updater.SideEffect> {

        val (chain, chainAsset) = stakingSharedState.assetWithChain.first()
        val runtime = chainRegistry.getRuntime(chain.id)

        val currentAccountId = scope.getAccount().accountIdIn(chain)!! // TODO ethereum

        val key = runtime.metadata.staking().storage("Bonded").storageKey(runtime, currentAccountId)

        return storageSubscriptionBuilder.subscribe(key)
            .flatMapLatest { change ->
                // assume we're controller, if no controller found
                val controllerId = change.value?.fromHex() ?: currentAccountId

                subscribeToLedger(storageSubscriptionBuilder.socketService, runtime, chain.id, controllerId)
            }.onEach { ledgerWithController ->
                updateAccountStaking(chain.id, chainAsset.id, currentAccountId, ledgerWithController)

                ledgerWithController?.let {
                    val era = stakingRepository.getActiveEraIndex(chain.id)

                    val stashId = it.ledger.stashId
                    val controllerId = it.controllerId

                    updateAssetStaking(it.ledger.stashId, chainAsset, it.ledger, era)

                    if (!stashId.contentEquals(controllerId)) {
                        updateAssetStaking(controllerId, chainAsset, it.ledger, era)
                    }
                } ?: updateAssetStakingForEmptyLedger(currentAccountId, chainAsset)
            }
            .flowOn(Dispatchers.IO)
            .noSideAffects()
    }

    private suspend fun updateAccountStaking(
        chainId: String,
        chainAssetId: Int,
        accountId: AccountId,
        ledgerWithController: LedgerWithController?,
    ) {

        val accountStaking = AccountStakingLocal(
            chainId = chainId,
            chainAssetId = chainAssetId,
            accountId = accountId,
            stakingAccessInfo = ledgerWithController?.let {
                AccountStakingLocal.AccessInfo(
                    stashId = it.ledger.stashId,
                    controllerId = it.controllerId,
                )
            }
        )

        accountStakingDao.insert(accountStaking)
    }

    private suspend fun subscribeToLedger(
        socketService: SocketService,
        runtime: RuntimeSnapshot,
        chainId: String,
        controllerId: AccountId,
    ): Flow<LedgerWithController?> {
        val key = runtime.metadata.staking().storage("Ledger").storageKey(runtime, controllerId)
        val request = SubscribeStorageRequest(key)

        return socketService.subscriptionFlow(request)
            .map { it.storageChange() }
            .onEach {
                val storageChange = StorageChange(it.block, key, it.getSingleChange())

                storageCache.insert(storageChange, chainId)
            }
            .map {
                val change = it.getSingleChange()

                if (change != null) {
                    val ledger = bindStakingLedger(change, runtime)

                    LedgerWithController(ledger, controllerId)
                } else {
                    null
                }
            }
    }

    private suspend fun updateAssetStaking(
        accountId: AccountId,
        chainAsset: Chain.Asset,
        stakingLedger: StakingLedger,
        era: BigInteger,
    ) {
        assetCache.updateAsset(accountId, chainAsset) { cached ->

            val redeemable = stakingLedger.sumStaking { it.isRedeemableIn(era) }
            val unbonding = stakingLedger.sumStaking { it.isUnbondingIn(era) }

            cached.copy(
                redeemableInPlanks = redeemable,
                unbondingInPlanks = unbonding,
                bondedInPlanks = stakingLedger.active
            )
        }
    }

    private suspend fun updateAssetStakingForEmptyLedger(
        accountId: AccountId,
        chainAsset: Chain.Asset,
    ) {
        assetCache.updateAsset(accountId, chainAsset) { cached ->
            cached.copy(
                redeemableInPlanks = BigInteger.ZERO,
                unbondingInPlanks = BigInteger.ZERO,
                bondedInPlanks = BigInteger.ZERO
            )
        }
    }
}
