package com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser

import androidx.lifecycle.lifecycleScope
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.feature_wallet_api.presentation.view.amount.ChooseAmountView
import com.edgeverse.wallet.feature_wallet_api.presentation.view.amount.setChooseAmountModel

fun BaseFragment<*>.setupAmountChooser(
    mixin: AmountChooserMixin,
    amountView: ChooseAmountView,
) {
    amountView.amountInput.bindTo(mixin.amountInput, lifecycleScope)

    mixin.assetModel.observe(amountView::setChooseAmountModel)
    mixin.fiatAmount.observe(amountView::setFiatAmount)
}
