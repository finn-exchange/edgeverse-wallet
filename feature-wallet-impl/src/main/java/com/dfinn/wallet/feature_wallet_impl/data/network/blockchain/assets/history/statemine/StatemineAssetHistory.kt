package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.history.statemine

import com.dfinn.wallet.common.data.network.runtime.binding.bindAccountIdentifier
import com.dfinn.wallet.common.data.network.runtime.binding.bindNumber
import com.dfinn.wallet.common.utils.assets
import com.dfinn.wallet.common.utils.oneOf
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.balances.TransferExtrinsic
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.balances.filterOwn
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.history.AssetHistory
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.TransactionFilter
import com.dfinn.wallet.runtime.ext.findAssetByStatemineId
import com.dfinn.wallet.runtime.ext.isUtilityAsset
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.getRuntime
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.status
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.GenericCall
import jp.co.soramitsu.fearless_utils.runtime.metadata.call

class StatemineAssetHistory(
    private val chainRegistry: ChainRegistry,
    private val eventsRepository: EventsRepository,
) : AssetHistory {

    override suspend fun fetchOperationsForBalanceChange(
        chain: Chain,
        blockHash: String,
        accountId: AccountId
    ): Result<List<TransferExtrinsic>> = runCatching {
        val runtime = chainRegistry.getRuntime(chain.id)
        val extrinsicsWithEvents = eventsRepository.getExtrinsicsWithEvents(chain.id, blockHash)

        extrinsicsWithEvents.filter { it.extrinsic.call.isTransfer(runtime) }
            .mapNotNull { extrinsicWithEvents ->
                val extrinsic = extrinsicWithEvents.extrinsic
                val chainAsset = chain.findAssetByStatemineId(bindNumber(extrinsic.call.arguments["id"]))

                chainAsset?.let {
                    TransferExtrinsic(
                        senderId = bindAccountIdentifier(extrinsic.signature!!.accountIdentifier),
                        recipientId = bindAccountIdentifier(extrinsic.call.arguments["target"]),
                        amountInPlanks = bindNumber(extrinsic.call.arguments["amount"]),
                        hash = extrinsicWithEvents.extrinsicHash,
                        chainAsset = chainAsset,
                        status = extrinsicWithEvents.status()
                    )
                }
            }.filterOwn(accountId)
    }

    override fun availableOperationFilters(asset: Chain.Asset): Set<TransactionFilter> {
        return setOfNotNull(
            TransactionFilter.TRANSFER,
            TransactionFilter.EXTRINSIC.takeIf { asset.isUtilityAsset }
        )
    }

    private fun GenericCall.Instance.isTransfer(runtime: RuntimeSnapshot): Boolean {
        val assets = runtime.metadata.assets()

        return oneOf(
            assets.call("transfer"),
            assets.call("transfer_keep_alive"),
        )
    }
}
