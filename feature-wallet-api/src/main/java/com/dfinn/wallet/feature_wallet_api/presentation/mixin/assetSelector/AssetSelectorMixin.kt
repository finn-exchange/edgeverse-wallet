package com.dfinn.wallet.feature_wallet_api.presentation.mixin.assetSelector

import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import com.dfinn.wallet.feature_wallet_api.presentation.model.AssetModel
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
