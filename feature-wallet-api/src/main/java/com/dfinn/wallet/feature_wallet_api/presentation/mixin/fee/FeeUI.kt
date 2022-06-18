package com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee

import com.dfinn.wallet.common.base.BaseFragmentMixin
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.impl.observeRetries
import com.dfinn.wallet.common.utils.makeGone
import com.dfinn.wallet.feature_wallet_api.presentation.view.FeeView

interface WithFeeLoaderMixin {

    val feeLoaderMixin: FeeLoaderMixin?
}

fun <V> BaseFragmentMixin<V>.setupFeeLoading(viewModel: V, feeView: FeeView) where V : BaseViewModel, V : FeeLoaderMixin {
    observeRetries(viewModel)

    viewModel.feeLiveData.observe(feeView::setFeeStatus)
}

fun BaseFragmentMixin<*>.setupFeeLoading(withFeeLoaderMixin: WithFeeLoaderMixin, feeView: FeeView) {
    val mixin = withFeeLoaderMixin.feeLoaderMixin

    if (mixin != null) {
        observeRetries(mixin)

        mixin.feeLiveData.observe(feeView::setFeeStatus)
    } else {
        feeView.makeGone()
    }
}
