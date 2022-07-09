package com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector

import coil.ImageLoader
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.view.AssetSelectorBottomSheet
import com.edgeverse.wallet.feature_wallet_api.presentation.view.AssetSelectorView

interface WithAssetSelector {

    val assetSelectorMixin: AssetSelectorMixin
}

fun <V> BaseFragment<V>.setupAssetSelector(
    view: AssetSelectorView,
    viewModel: V,
    imageLoader: ImageLoader
) where V : BaseViewModel, V : WithAssetSelector {
    view.onClick {
        viewModel.assetSelectorMixin.assetSelectorClicked()
    }

    viewModel.assetSelectorMixin.selectedAssetModelFlow.observe {
        view.setState(imageLoader, it)
    }

    viewModel.assetSelectorMixin.showAssetChooser.observeEvent {
        AssetSelectorBottomSheet(
            imageLoader = imageLoader,
            context = requireContext(),
            payload = it,
            onClicked = viewModel.assetSelectorMixin::assetChosen
        ).show()
    }
}
