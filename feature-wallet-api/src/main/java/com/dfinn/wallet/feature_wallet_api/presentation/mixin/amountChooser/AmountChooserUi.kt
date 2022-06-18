package com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser

import androidx.lifecycle.lifecycleScope
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.utils.bindTo
import com.dfinn.wallet.feature_wallet_api.presentation.view.amount.ChooseAmountView
import com.dfinn.wallet.feature_wallet_api.presentation.view.amount.setChooseAmountModel

fun BaseFragment<*>.setupAmountChooser(
    mixin: AmountChooserMixin,
    amountView: ChooseAmountView,
) {
    amountView.amountInput.bindTo(mixin.amountInput, lifecycleScope)

    mixin.assetModel.observe(amountView::setChooseAmountModel)
    mixin.fiatAmount.observe(amountView::setFiatAmount)
}
