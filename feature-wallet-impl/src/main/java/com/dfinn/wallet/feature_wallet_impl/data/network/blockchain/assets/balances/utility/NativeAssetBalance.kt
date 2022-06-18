package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.balances.utility

import android.util.Log
import com.dfinn.wallet.common.data.network.runtime.binding.BlockHash
import com.dfinn.wallet.common.utils.LOG_TAG
import com.dfinn.wallet.common.utils.balances
import com.dfinn.wallet.common.utils.numberConstant
import com.dfinn.wallet.common.utils.system
import com.dfinn.wallet.core.updater.SubscriptionBuilder
import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import com.dfinn.wallet.feature_wallet_api.data.cache.AssetCache
import com.dfinn.wallet.feature_wallet_api.data.cache.bindAccountInfoOrDefault
import com.dfinn.wallet.feature_wallet_api.data.cache.updateAsset
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.balances.AssetBalance
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.SubstrateRemoteSource
import com.dfinn.wallet.runtime.ext.utilityAsset
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.getRuntime
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.math.BigInteger

class NativeAssetBalance(
    private val chainRegistry: ChainRegistry,
    private val assetCache: AssetCache,
    private val substrateRemoteSource: SubstrateRemoteSource,
) : AssetBalance {

    override suspend fun existentialDeposit(chain: Chain, chainAsset: Chain.Asset): BigInteger {
        val runtime = chainRegistry.getRuntime(chain.id)

        return runtime.metadata.balances().numberConstant("ExistentialDeposit", runtime)
    }

    override suspend fun queryTotalBalance(chain: Chain, chainAsset: Chain.Asset, accountId: AccountId): BigInteger {
        val accountInfo = substrateRemoteSource.getAccountInfo(chain.id, accountId)

        return accountInfo.data.free + accountInfo.data.reserved
    }

    override suspend fun startSyncingBalance(
        chain: Chain,
        chainAsset: Chain.Asset,
        metaAccount: MetaAccount,
        accountId: AccountId,
        subscriptionBuilder: SubscriptionBuilder
    ): Flow<BlockHash?> {
        val runtime = chainRegistry.getRuntime(chain.id)

        val key = try {
            runtime.metadata.system().storage("Account").storageKey(runtime, accountId)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to construct account storage key: ${e.message} in ${chain.name}")

            return emptyFlow()
        }

        return subscriptionBuilder.subscribe(key)
            .map { change ->
                val accountInfo = bindAccountInfoOrDefault(change.value, runtime)
                val assetChanged = assetCache.updateAsset(metaAccount.id, chain.utilityAsset, accountInfo)

                change.block.takeIf { assetChanged }
            }
    }
}
