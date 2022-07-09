package com.edgeverse.wallet.feature_wallet_api.domain.validation

import com.edgeverse.wallet.common.validation.DefaultFailureLevel
import com.edgeverse.wallet.common.validation.Validation
import com.edgeverse.wallet.common.validation.ValidationStatus
import com.edgeverse.wallet.common.validation.ValidationSystemBuilder
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import java.math.BigDecimal

class EnoughToPayFeesValidation<P, E>(
    private val feeExtractor: AmountProducer<P>,
    private val availableBalanceProducer: AmountProducer<P>,
    private val errorProducer: (P) -> E,
    private val extraAmountExtractor: AmountProducer<P> = { BigDecimal.ZERO },
) : Validation<P, E> {

    companion object;

    override suspend fun validate(value: P): ValidationStatus<E> {
        val fee = feeExtractor(value)
        val available = availableBalanceProducer(value)
        val amount = extraAmountExtractor(value)

        return if (fee + amount <= available) {
            ValidationStatus.Valid()
        } else {
            ValidationStatus.NotValid(DefaultFailureLevel.ERROR, errorProducer(value))
        }
    }
}

fun <P, E> ValidationSystemBuilder<P, E>.sufficientBalance(
    fee: AmountProducer<P> = { BigDecimal.ZERO },
    amount: AmountProducer<P> = { BigDecimal.ZERO },
    available: AmountProducer<P>,
    error: (P) -> E
) = validate(
    EnoughToPayFeesValidation(
        feeExtractor = fee,
        extraAmountExtractor = amount,
        errorProducer = error,
        availableBalanceProducer = available
    )
)

fun <P> EnoughToPayFeesValidation.Companion.assetBalanceProducer(
    walletRepository: WalletRepository,
    stakingSharedState: SingleAssetSharedState,
    originAddressExtractor: (P) -> String,
    chainAssetExtractor: (P) -> Chain.Asset,
): AmountProducer<P> = { payload ->
    val chain = stakingSharedState.chain()
    val accountId = chain.accountIdOf(originAddressExtractor(payload))

    val asset = walletRepository.getAsset(accountId, chainAssetExtractor(payload))!!

    asset.transferable
}
