package com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers

import com.dfinn.wallet.common.validation.Validation
import com.dfinn.wallet.common.validation.ValidationSystem
import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import com.dfinn.wallet.feature_wallet_api.domain.model.planksFromAmount
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal
import java.math.BigInteger

typealias AssetTransfersValidationSystem = ValidationSystem<AssetTransferPayload, AssetTransferValidationFailure>
typealias AssetTransfersValidation = Validation<AssetTransferPayload, AssetTransferValidationFailure>

sealed class AssetTransferValidationFailure {

    sealed class WillRemoveAccount : AssetTransferValidationFailure() {
        object WillBurnDust : WillRemoveAccount()

        class WillTransferDust(val dust: BigDecimal) : WillRemoveAccount()
    }

    sealed class DeadRecipient : AssetTransferValidationFailure() {

        object InUsedAsset : DeadRecipient()

        class InCommissionAsset(val commissionAsset: Chain.Asset) : DeadRecipient()
    }

    sealed class NotEnoughFunds : AssetTransferValidationFailure() {
        object InUsedAsset : NotEnoughFunds()

        class InCommissionAsset(val commissionAsset: Chain.Asset) : NotEnoughFunds()
    }

    class InvalidRecipientAddress(val chain: Chain) : AssetTransferValidationFailure()

    class PhishingRecipient(val address: String) : AssetTransferValidationFailure()

    object NonPositiveAmount : AssetTransferValidationFailure()
}

data class AssetTransferPayload(
    val transfer: AssetTransfer,
    val fee: BigDecimal,
    val commissionAsset: Asset,
    val usedAsset: Asset
)

val AssetTransferPayload.sendingCommissionAsset
    get() = usedAsset.token.configuration == commissionAsset.token.configuration

val AssetTransferPayload.feeInUsedAsset: BigDecimal
    get() = if (sendingCommissionAsset) {
        fee
    } else {
        BigDecimal.ZERO
    }

val AssetTransferPayload.amountInCommissionAsset: BigInteger
    get() = if (sendingCommissionAsset) {
        transfer.amountInPlanks
    } else {
        BigInteger.ZERO
    }

val AssetTransfer.amountInPlanks
    get() = chainAsset.planksFromAmount(amount)
