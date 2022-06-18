package com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers

import com.dfinn.wallet.common.validation.ValidationSystem
import com.dfinn.wallet.common.validation.ValidationSystemBuilder
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.model.accountIdIn
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferPayload
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferValidationFailure
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferValidationFailure.WillRemoveAccount
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfers
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfersValidationSystem
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.amountInCommissionAsset
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.amountInPlanks
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.feeInUsedAsset
import com.dfinn.wallet.feature_wallet_api.domain.model.amountFromPlanks
import com.dfinn.wallet.feature_wallet_api.domain.validation.ExistentialDepositError
import com.dfinn.wallet.feature_wallet_api.domain.validation.PhishingValidationFactory
import com.dfinn.wallet.feature_wallet_api.domain.validation.doNotCrossExistentialDeposit
import com.dfinn.wallet.feature_wallet_api.domain.validation.enoughTotalToStayAboveED
import com.dfinn.wallet.feature_wallet_api.domain.validation.notPhishingAccount
import com.dfinn.wallet.feature_wallet_api.domain.validation.positiveAmount
import com.dfinn.wallet.feature_wallet_api.domain.validation.sufficientBalance
import com.dfinn.wallet.feature_wallet_api.domain.validation.validAddress
import com.dfinn.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers.validations.notDeadRecipient
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.getRuntime
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.runtime.metadata.callOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.moduleOrNull
import java.math.BigDecimal
import java.math.BigInteger

typealias AssetTransfersValidationSystemBuilder = ValidationSystemBuilder<AssetTransferPayload, AssetTransferValidationFailure>

abstract class BaseAssetTransfers(
    private val chainRegistry: ChainRegistry,
    private val assetSourceRegistry: AssetSourceRegistry,
    private val extrinsicService: ExtrinsicService,
    private val phishingValidationFactory: PhishingValidationFactory,
) : AssetTransfers {

    protected abstract fun ExtrinsicBuilder.transfer(transfer: AssetTransfer)

    /**
     * Format: [(Module, Function)]
     * Transfers will be enabled if at least one function exists
     */
    protected abstract val transferFunctions: List<Pair<String, String>>

    override suspend fun performTransfer(transfer: AssetTransfer): Result<String> {
        val senderAccountId = transfer.sender.accountIdIn(transfer.chain)!!

        return extrinsicService.submitExtrinsic(transfer.chain, senderAccountId) {
            transfer(transfer)
        }
    }

    override suspend fun calculateFee(transfer: AssetTransfer): BigInteger {
        return extrinsicService.estimateFee(transfer.chain) {
            transfer(transfer)
        }
    }

    override suspend fun areTransfersEnabled(chainAsset: Chain.Asset): Boolean {
        val runtime = chainRegistry.getRuntime(chainAsset.chainId)

        return transferFunctions.any { (module, function) ->
            runtime.metadata.moduleOrNull(module)?.callOrNull(function) != null
        }
    }

    private suspend fun existentialDepositForUsedAsset(transfer: AssetTransfer): BigDecimal {
        return existentialDeposit(transfer.chain, transfer.chainAsset)
    }

    private suspend fun existentialDeposit(chain: Chain, asset: Chain.Asset): BigDecimal {
        val inPlanks = assetSourceRegistry.sourceFor(asset).balance
            .existentialDeposit(chain, asset)

        return asset.amountFromPlanks(inPlanks)
    }

    protected fun defaultValidationSystem(
        removeAccountBehavior: ExistentialDepositError<WillRemoveAccount>
    ): AssetTransfersValidationSystem = ValidationSystem {
        validAddress()

        notPhishingRecipient()

        positiveAmount()

        sufficientTransferableBalanceToPayFee()
        sufficientBalanceInUsedAsset()

        sufficientCommissionBalanceToStayAboveED()

        notDeadRecipientInUsedAsset()
        notDeadRecipientInCommissionAsset()

        doNotCrossExistentialDeposit(removeAccountBehavior)
    }

    private fun AssetTransfersValidationSystemBuilder.notPhishingRecipient() = notPhishingAccount(
        factory = phishingValidationFactory,
        address = { it.transfer.recipient },
        chain = { it.transfer.chain },
        warning = AssetTransferValidationFailure::PhishingRecipient
    )

    private fun AssetTransfersValidationSystemBuilder.validAddress() = validAddress(
        address = { it.transfer.recipient },
        chain = { it.transfer.chain },
        error = { AssetTransferValidationFailure.InvalidRecipientAddress(it.transfer.chain) }
    )

    protected fun AssetTransfersValidationSystemBuilder.positiveAmount() = positiveAmount(
        amount = { it.transfer.amount },
        error = { AssetTransferValidationFailure.NonPositiveAmount }
    )

    protected fun AssetTransfersValidationSystemBuilder.sufficientBalanceInUsedAsset() = sufficientBalance(
        amount = { it.transfer.amount },
        available = { it.usedAsset.transferable },
        error = { AssetTransferValidationFailure.NotEnoughFunds.InUsedAsset },
        fee = { it.feeInUsedAsset }
    )

    protected fun AssetTransfersValidationSystemBuilder.notDeadRecipientInUsedAsset() = notDeadRecipient(
        assetSourceRegistry = assetSourceRegistry,
        assetToCheck = { it.usedAsset },
        addingAmount = { it.transfer.amountInPlanks },
        failure = { AssetTransferValidationFailure.DeadRecipient.InUsedAsset }
    )

    protected fun AssetTransfersValidationSystemBuilder.notDeadRecipientInCommissionAsset() = notDeadRecipient(
        assetSourceRegistry = assetSourceRegistry,
        assetToCheck = { it.commissionAsset },
        addingAmount = { it.amountInCommissionAsset },
        failure = { AssetTransferValidationFailure.DeadRecipient.InCommissionAsset(commissionAsset = it.commissionAsset.token.configuration) }
    )

    protected fun AssetTransfersValidationSystemBuilder.sufficientTransferableBalanceToPayFee() = sufficientBalance(
        fee = { it.fee },
        available = { it.commissionAsset.transferable },
        error = { AssetTransferValidationFailure.NotEnoughFunds.InCommissionAsset(commissionAsset = it.commissionAsset.token.configuration) }
    )

    protected fun AssetTransfersValidationSystemBuilder.sufficientCommissionBalanceToStayAboveED() = enoughTotalToStayAboveED(
        fee = { it.fee },
        total = { it.commissionAsset.total },
        existentialDeposit = { existentialDeposit(it.transfer.chain, it.commissionAsset.token.configuration) },
        error = { AssetTransferValidationFailure.NotEnoughFunds.InCommissionAsset(commissionAsset = it.commissionAsset.token.configuration) }
    )

    protected fun AssetTransfersValidationSystemBuilder.doNotCrossExistentialDeposit(
        error: ExistentialDepositError<WillRemoveAccount>,
    ) = doNotCrossExistentialDeposit(
        totalBalance = { it.usedAsset.total },
        fee = { it.feeInUsedAsset },
        extraAmount = { it.transfer.amount },
        existentialDeposit = { existentialDepositForUsedAsset(it.transfer) },
        error = error
    )
}
