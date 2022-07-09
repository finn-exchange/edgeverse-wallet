package com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser

import androidx.annotation.StringRes
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.WithCoroutineScopeExtensions
import com.edgeverse.wallet.common.utils.formatAsCurrency
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.model.ChooseAmountModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

private const val DEBOUNCE_DURATION_MILLIS = 500

class AmountChooserProviderFactory(
    private val resourceManager: ResourceManager
) : AmountChooserMixin.Factory {

    override fun create(
        scope: CoroutineScope,
        assetFlow: Flow<Asset>,
        balanceField: (Asset) -> BigDecimal,
        @StringRes balanceLabel: Int?
    ): AmountChooserMixin.Presentation {
        return AmountChooserProvider(
            coroutineScope = scope,
            usedAssetFlow = assetFlow,
            balanceField = balanceField,
            balanceLabel = balanceLabel,
            resourceManager = resourceManager
        )
    }
}

@OptIn(ExperimentalTime::class)
class AmountChooserProvider(
    coroutineScope: CoroutineScope,
    override val usedAssetFlow: Flow<Asset>,
    private val resourceManager: ResourceManager,
    private val balanceField: (Asset) -> BigDecimal,
    @StringRes private val balanceLabel: Int?
) : AmountChooserMixin.Presentation,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    override val amountInput: MutableStateFlow<String> = MutableStateFlow("")

    override val assetModel = usedAssetFlow
        .map { ChooseAmountModel(it, resourceManager, balanceField, balanceLabel) }
        .inBackground()
        .share()

    override val amount: Flow<BigDecimal> = amountInput
        .map { it.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        .share()

    override val backPressuredAmount: Flow<BigDecimal> = amount
        .debounce(DEBOUNCE_DURATION_MILLIS.milliseconds)

    override val fiatAmount: Flow<String> = usedAssetFlow.combine(amount) { asset, amount ->
        asset.token.fiatAmount(amount).formatAsCurrency()
    }
        .inBackground()
        .share()
}
