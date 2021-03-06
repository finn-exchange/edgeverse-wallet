package com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.validations

import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.ValidationSystemBuilder
import com.edgeverse.wallet.common.validation.validOrError
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferPayload
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferValidationFailure
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfersValidation
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.validation.PlanksProducer
import com.edgeverse.wallet.runtime.ext.accountIdOf
import java.math.BigInteger

class DeadRecipientValidation(
    private val assetSourceRegistry: AssetSourceRegistry,
    private val addingAmount: PlanksProducer<AssetTransferPayload>,
    private val assetToCheck: (AssetTransferPayload) -> Asset,
    private val failure: (AssetTransferPayload) -> AssetTransferValidationFailure.DeadRecipient,
) : AssetTransfersValidation {

    override suspend fun validate(value: AssetTransferPayload): ValidationStatus<AssetTransferValidationFailure> {
        val chain = value.transfer.chain
        val chainAsset = assetToCheck(value).token.configuration

        val balanceSource = assetSourceRegistry.sourceFor(chainAsset).balance

        val existentialDeposit = balanceSource.existentialDeposit(chain, chainAsset)
        val recipientAccountId = value.transfer.chain.accountIdOf(value.transfer.recipient)

        val recipientBalance = balanceSource.queryTotalBalance(chain, chainAsset, recipientAccountId)

        return validOrError(recipientBalance + addingAmount(value) >= existentialDeposit) {
            failure(value)
        }
    }
}

fun ValidationSystemBuilder<AssetTransferPayload, AssetTransferValidationFailure>.notDeadRecipient(
    assetSourceRegistry: AssetSourceRegistry,
    failure: (AssetTransferPayload) -> AssetTransferValidationFailure.DeadRecipient,
    assetToCheck: (AssetTransferPayload) -> Asset,
    addingAmount: PlanksProducer<AssetTransferPayload> = { BigInteger.ZERO },
) = validate(
    DeadRecipientValidation(
        assetSourceRegistry = assetSourceRegistry,
        addingAmount = addingAmount,
        assetToCheck = assetToCheck,
        failure = failure
    )
)
