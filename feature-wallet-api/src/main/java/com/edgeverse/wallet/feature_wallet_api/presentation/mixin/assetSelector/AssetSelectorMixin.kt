package com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector

import androidx.lifecycle.LiveData
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AssetModel
import kotlinx.coroutines.flow.Flow

interface AssetSelectorMixin {

    val showAssetChooser: LiveData<Event<DynamicListBottomSheet.Payload<AssetModel>>>

    fun assetSelectorClicked()

    fun assetChosen(assetModel: AssetModel)

    val selectedAssetModelFlow: Flow<AssetModel>

    interface Presentation : AssetSelectorMixin {

        val selectedAssetFlow: Flow<Asset>
    }
}
