package com.dfinn.wallet.feature_wallet_api.presentation.mixin.assetSelector

import coil.ImageLoader
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.feature_wallet_api.presentation.view.AssetSelectorBottomSheet
import com.dfinn.wallet.feature_wallet_api.presentation.view.AssetSelectorView

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
