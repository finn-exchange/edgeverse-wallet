package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.utility

import com.dfinn.wallet.common.utils.Modules
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferValidationFailure.WillRemoveAccount
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfersValidationSystem
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.nativeTransfer
import com.dfinn.wallet.feature_wallet_api.domain.model.planksFromAmount
import com.dfinn.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.BaseAssetTransfers
import com.dfinn.wallet.runtime.ext.accountIdOrDefault
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder

class NativeAssetTransfers(
    chainRegistry: ChainRegistry,
    assetSourceRegistry: AssetSourceRegistry,
    extrinsicService: ExtrinsicService,
    phishingValidationFactory: PhishingValidationFactory,
) : BaseAssetTransfers(chainRegistry, assetSourceRegistry, extrinsicService, phishingValidationFactory) {

    override val validationSystem: AssetTransfersValidationSystem = defaultValidationSystem(
        removeAccountBehavior = { WillRemoveAccount.WillBurnDust }
    )

    override fun ExtrinsicBuilder.transfer(transfer: AssetTransfer) {
        nativeTransfer(
            accountId = transfer.chain.accountIdOrDefault(transfer.recipient),
            amount = transfer.chainAsset.planksFromAmount(transfer.amount)
        )
    }

    override val transferFunctions = listOf(Modules.BALANCES to "transfer")
}
