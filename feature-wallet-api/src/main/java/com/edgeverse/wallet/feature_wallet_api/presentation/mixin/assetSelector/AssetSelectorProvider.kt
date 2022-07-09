package com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.feature_wallet_api.data.mappers.mapAssetToAssetModel
import com.edgeverse.wallet.feature_wallet_api.domain.AssetUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AssetModel
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class AssetSelectorFactory(
    private val assetUseCase: AssetUseCase,
    private val singleAssetSharedState: SingleAssetSharedState,
    private val resourceManager: ResourceManager
) : MixinFactory<AssetSelectorMixin.Presentation> {

    override fun create(scope: CoroutineScope): AssetSelectorMixin.Presentation {
        return AssetSelectorProvider(assetUseCase, resourceManager, singleAssetSharedState, scope)
    }
}

private class AssetSelectorProvider(
    private val assetUseCase: AssetUseCase,
    private val resourceManager: ResourceManager,
    private val singleAssetSharedState: SingleAssetSharedState,
    private val scope: CoroutineScope,
) : AssetSelectorMixin.Presentation, CoroutineScope by scope {

    override val showAssetChooser = MutableLiveData<Event<DynamicListBottomSheet.Payload<AssetModel>>>()

    override val selectedAssetFlow: Flow<Asset> = assetUseCase.currentAssetFlow()
        .inBackground()
        .shareIn(this, SharingStarted.Eagerly, replay = 1)

    override val selectedAssetModelFlow: Flow<AssetModel> = selectedAssetFlow
        .map {
            mapAssetToAssetModel(it, resourceManager, patternId = null)
        }
        .shareIn(this, SharingStarted.Eagerly, replay = 1)

    override fun assetSelectorClicked() {
        launch {
            val availableToSelect = assetUseCase.availableAssetsToSelect()

            val models = availableToSelect.map { mapAssetToAssetModel(it, resourceManager, patternId = null) }

            val selectedChainAsset = selectedAssetFlow.first().token.configuration

            val selectedModel = models.firstOrNull { it.chainAssetId == selectedChainAsset.id && it.chainId == selectedChainAsset.chainId }

            showAssetChooser.value = Event(DynamicListBottomSheet.Payload(models, selectedModel))
        }
    }

    override fun assetChosen(assetModel: AssetModel) {
        singleAssetSharedState.update(assetModel.chainId, assetModel.chainAssetId)
    }
}
