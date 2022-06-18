package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.statemine

import com.dfinn.wallet.common.utils.Modules
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferValidationFailure.WillRemoveAccount
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfersValidationSystem
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.amountInPlanks
import com.dfinn.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.BaseAssetTransfers
import com.dfinn.wallet.runtime.ext.accountIdOrDefault
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.instances.AddressInstanceConstructor
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import java.math.BigInteger

class StatemineAssetTransfers(
    chainRegistry: ChainRegistry,
    assetSourceRegistry: AssetSourceRegistry,
    extrinsicService: ExtrinsicService,
    phishingValidationFactory: PhishingValidationFactory,
) : BaseAssetTransfers(chainRegistry, assetSourceRegistry, extrinsicService, phishingValidationFactory) {

    override val validationSystem: AssetTransfersValidationSystem = defaultValidationSystem(
        removeAccountBehavior = WillRemoveAccount::WillTransferDust
    )

    override val transferFunctions = listOf(Modules.ASSETS to "transfer")

    override fun ExtrinsicBuilder.transfer(transfer: AssetTransfer) {
        val chainAssetType = transfer.chainAsset.type
        require(chainAssetType is Chain.Asset.Type.Statemine)

        statemineTransfer(
            assetId = chainAssetType.id,
            target = transfer.chain.accountIdOrDefault(transfer.recipient),
            amount = transfer.amountInPlanks
        )
    }

    private fun ExtrinsicBuilder.statemineTransfer(
        assetId: BigInteger,
        target: AccountId,
        amount: BigInteger
    ) {
        call(
            moduleName = Modules.ASSETS,
            callName = "transfer",
            arguments = mapOf(
                "id" to assetId,
                "target" to AddressInstanceConstructor.constructInstance(runtime.typeRegistry, target),
                "amount" to amount
            )
        )
    }
}
