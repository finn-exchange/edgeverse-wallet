package com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.orml

import com.edgeverse.wallet.common.utils.Modules
import com.edgeverse.wallet.common.utils.firstExistingModule
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferValidationFailure.WillRemoveAccount
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfersValidationSystem
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.amountInPlanks
import com.edgeverse.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.BaseAssetTransfers
import com.edgeverse.wallet.runtime.ext.accountIdOrDefault
import com.edgeverse.wallet.runtime.ext.ormlCurrencyId
import com.edgeverse.wallet.runtime.ext.requireOrml
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.instances.AddressInstanceConstructor
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import java.math.BigInteger

class OrmlAssetTransfers(
    chainRegistry: ChainRegistry,
    assetSourceRegistry: AssetSourceRegistry,
    extrinsicService: ExtrinsicService,
    phishingValidationFactory: PhishingValidationFactory,
) : BaseAssetTransfers(chainRegistry, assetSourceRegistry, extrinsicService, phishingValidationFactory) {

    override fun ExtrinsicBuilder.transfer(transfer: AssetTransfer) {
        ormlTransfer(
            chainAsset = transfer.chainAsset,
            target = transfer.chain.accountIdOrDefault(transfer.recipient),
            amount = transfer.amountInPlanks
        )
    }

    override val transferFunctions = listOf(
        Modules.CURRENCIES to "transfer",
        Modules.TOKENS to "transfer"
    )

    override suspend fun areTransfersEnabled(chainAsset: Chain.Asset): Boolean {
        // flag from chains json AND existence of module & function in runtime metadata
        return chainAsset.requireOrml().transfersEnabled && super.areTransfersEnabled(chainAsset)
    }

    override val validationSystem: AssetTransfersValidationSystem = defaultValidationSystem(
        removeAccountBehavior = { WillRemoveAccount.WillBurnDust }
    )

    private fun ExtrinsicBuilder.ormlTransfer(
        chainAsset: Chain.Asset,
        target: AccountId,
        amount: BigInteger
    ) {
        call(
            moduleName = runtime.metadata.firstExistingModule(Modules.CURRENCIES, Modules.TOKENS),
            callName = "transfer",
            arguments = mapOf(
                "dest" to AddressInstanceConstructor.constructInstance(runtime.typeRegistry, target),
                "currency_id" to chainAsset.ormlCurrencyId(runtime),
                "amount" to amount
            )
        )
    }
}
