package com.dfinn.wallet.feature_nft_impl.presentation.nft.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.presentation.LoadingState
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.format
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.mapList
import com.dfinn.wallet.feature_nft_api.data.model.Nft
import com.dfinn.wallet.feature_nft_impl.NftRouter
import com.dfinn.wallet.feature_nft_impl.domain.nft.list.NftListInteractor
import com.dfinn.wallet.feature_nft_impl.domain.nft.list.PricedNft
import com.dfinn.wallet.feature_nft_impl.presentation.nft.common.formatIssuance
import com.dfinn.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NftListViewModel(
    private val router: NftRouter,
    private val resourceManager: ResourceManager,
    private val interactor: NftListInteractor,
) : BaseViewModel() {

    private val nftsFlow = interactor.userNftsFlow()
        .inBackground()
        .share()

    val nftCountFlow = nftsFlow.map { it.size.format() }
        .share()

    val nftListItemsFlow = nftsFlow.mapList(::mapNftToListItem)
        .inBackground()
        .share()

    private val _hideRefreshEvent = MutableLiveData<Event<Unit>>()
    val hideRefreshEvent: LiveData<Event<Unit>> = _hideRefreshEvent

    fun syncNfts() {
        viewModelScope.launch {
            interactor.syncNftsList()

            _hideRefreshEvent.value = Event(Unit)
        }
    }

    fun nftClicked(nftListItem: NftListItem) = launch {
        if (nftListItem.content is LoadingState.Loaded) {
            router.openNftDetails(nftListItem.identifier)
        }
    }

    fun loadableNftShown(nftListItem: NftListItem) = launch(Dispatchers.Default) {
        val pricedNft = nftsFlow.first().firstOrNull { it.nft.identifier == nftListItem.identifier }
            ?: return@launch

        interactor.fullSyncNft(pricedNft.nft)
    }

    private fun mapNftToListItem(pricedNft: PricedNft): NftListItem {
        val content = when (val details = pricedNft.nft.details) {
            Nft.Details.Loadable -> LoadingState.Loading()

            is Nft.Details.Loaded -> {
                val issuanceFormatted = resourceManager.formatIssuance(details.issuance)

                val amountModel = if (details.price != null && pricedNft.nftPriceToken != null) {
                    mapAmountToAmountModel(details.price!!, pricedNft.nftPriceToken)
                } else {
                    null
                }

                LoadingState.Loaded(
                    NftListItem.Content(
                        issuance = issuanceFormatted,
                        title = details.metadata?.name ?: pricedNft.nft.instanceId ?: pricedNft.nft.collectionId,
                        price = amountModel,
                        media = details.metadata?.media
                    )
                )
            }
        }

        return NftListItem(
            identifier = pricedNft.nft.identifier,
            content = content
        )
    }

    fun backClicked() {
        router.back()
    }
}
