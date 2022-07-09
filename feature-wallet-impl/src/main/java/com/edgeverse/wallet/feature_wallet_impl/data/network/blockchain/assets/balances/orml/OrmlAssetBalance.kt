package com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.balances.orml

import com.edgeverse.wallet.common.data.network.runtime.binding.BlockHash
import com.edgeverse.wallet.common.utils.tokens
import com.edgeverse.wallet.core.updater.SubscriptionBuilder
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_wallet_api.data.cache.AssetCache
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.balances.AssetBalance
import com.edgeverse.wallet.runtime.ext.ormlCurrencyId
import com.edgeverse.wallet.runtime.ext.requireOrml
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigInteger

class OrmlAssetBalance(
    private val assetCache: AssetCache,
    private val storageSource: StorageDataSource,
    private val chainRegistry: ChainRegistry,
) : AssetBalance {

    override suspend fun existentialDeposit(chain: Chain, chainAsset: Chain.Asset): BigInteger {
        return chainAsset.requireOrml().existentialDeposit
    }

    override suspend fun queryTotalBalance(chain: Chain, chainAsset: Chain.Asset, accountId: AccountId): BigInteger {
        val ormlAccountData = storageSource.query(
            chainId = chain.id,
            keyBuilder = { it.ormlBalanceKey(accountId, chainAsset) },
            binding = { scale, runtime -> bindOrmlAccountDataOrEmpty(scale, runtime) }
        )

        return ormlAccountData.free + ormlAccountData.reserved
    }

    override suspend fun startSyncingBalance(
        chain: Chain,
        chainAsset: Chain.Asset,
        metaAccount: MetaAccount,
        accountId: AccountId,
        subscriptionBuilder: SubscriptionBuilder
    ): Flow<BlockHash?> {
        val runtime = chainRegistry.getRuntime(chain.id)

        return subscriptionBuilder.subscribe(runtime.ormlBalanceKey(accountId, chainAsset))
            .map {
                val ormlAccountData = bindOrmlAccountDataOrEmpty(it.value, runtime)

                val assetChanged = updateAssetBalance(metaAccount.id, chainAsset, ormlAccountData)

                it.block.takeIf { assetChanged }
            }
    }

    private suspend fun updateAssetBalance(
        metaId: Long,
        chainAsset: Chain.Asset,
        ormlAccountData: OrmlAccountData
    ) = assetCache.updateAsset(metaId, chainAsset) {
        it.copy(
            frozenInPlanks = ormlAccountData.frozen,
            freeInPlanks = ormlAccountData.free,
            reservedInPlanks = ormlAccountData.reserved
        )
    }

    private fun RuntimeSnapshot.ormlBalanceKey(accountId: AccountId, chainAsset: Chain.Asset): String {
        return metadata.tokens().storage("Accounts").storageKey(this, accountId, chainAsset.ormlCurrencyId(this))
    }

    private fun bindOrmlAccountDataOrEmpty(scale: String?, runtime: RuntimeSnapshot): OrmlAccountData {
        return scale?.let { bindOrmlAccountData(it, runtime) } ?: OrmlAccountData.empty()
    }
}
