package com.dfinn.wallet.feature_dapp_impl.presentation.addToFavourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.sendEvent
import com.dfinn.wallet.common.utils.singleReplaySharedFlow
import com.dfinn.wallet.feature_dapp_impl.DAppRouter
import com.dfinn.wallet.feature_dapp_impl.data.model.FavouriteDApp
import com.dfinn.wallet.feature_dapp_impl.domain.browser.addToFavourites.AddToFavouritesInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddToFavouritesViewModel(
    private val interactor: AddToFavouritesInteractor,
    private val payload: AddToFavouritesPayload,
    private val router: DAppRouter,
) : BaseViewModel() {

    val urlFlow = MutableStateFlow(payload.url)
    val labelFlow = singleReplaySharedFlow<String>()

    val iconLink = singleReplaySharedFlow<String?>()

    private val _focusOnUrlFieldEvent = MutableLiveData<Event<Unit>>()
    val focusOnAddressFieldEvent: LiveData<Event<Unit>> = _focusOnUrlFieldEvent

    init {
        setInitialValues()
    }

    fun saveClicked() = launch {
        val favouriteDApp = FavouriteDApp(
            url = urlFlow.value,
            label = labelFlow.first(),
            icon = iconLink.first()
        )

        interactor.addToFavourites(favouriteDApp)

        router.back()
    }

    private fun setInitialValues() = launch {
        val resolvedDAppDisplay = interactor.resolveFavouriteDAppDisplay(url = payload.url, suppliedLabel = payload.label)

        labelFlow.emit(resolvedDAppDisplay.label)
        iconLink.emit(resolvedDAppDisplay.icon)

        _focusOnUrlFieldEvent.sendEvent()
    }

    fun backClicked() {
        router.back()
    }
}
