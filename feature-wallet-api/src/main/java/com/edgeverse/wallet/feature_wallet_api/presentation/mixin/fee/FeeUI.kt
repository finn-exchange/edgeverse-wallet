package com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee

import com.edgeverse.wallet.common.base.BaseFragmentMixin
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.impl.observeRetries
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.feature_wallet_api.presentation.view.FeeView

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
