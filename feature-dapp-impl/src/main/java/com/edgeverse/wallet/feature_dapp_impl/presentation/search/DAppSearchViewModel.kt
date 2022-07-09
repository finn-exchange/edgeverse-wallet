package com.edgeverse.wallet.feature_dapp_impl.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.list.headers.TextHeader
import com.edgeverse.wallet.common.list.toListWithHeaders
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.common.utils.sendEvent
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.domain.search.DappSearchGroup
import com.edgeverse.wallet.feature_dapp_impl.domain.search.DappSearchResult
import com.edgeverse.wallet.feature_dapp_impl.domain.search.SearchDappInteractor
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.model.DappSearchModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
class DAppSearchViewModel(
    private val router: DAppRouter,
    private val resourceManager: ResourceManager,
    private val interactor: SearchDappInteractor,
    private val payload: SearchPayload,
    private val dAppSearchResponder: DAppSearchResponder,
) : BaseViewModel() {

    val query = MutableStateFlow(payload.initialUrl.orEmpty())

    private val _selectQueryTextEvent = MutableLiveData<Event<Unit>>()
    val selectQueryTextEvent: LiveData<Event<Unit>> = _selectQueryTextEvent

    val searchResults = query
        .mapLatest {
            interactor.searchDapps(it)
                .mapKeys { (searchGroup, _) -> mapSearchGroupToTextHeader(searchGroup) }
                .mapValues { (_, groupItems) -> groupItems.map(::mapSearchResultToSearchModel) }
                .toListWithHeaders()
        }
        .inBackground()
        .share()

    init {
        if (!payload.initialUrl.isNullOrEmpty()) {
            _selectQueryTextEvent.sendEvent()
        }
    }

    fun cancelClicked() {
        if (shouldReportResult()) {
            dAppSearchResponder.respond(DAppSearchCommunicator.Response(newUrl = null))
        }

        router.back()
    }

    private fun mapSearchGroupToTextHeader(searchGroup: DappSearchGroup): TextHeader {
        val content = when (searchGroup) {
            DappSearchGroup.DAPPS -> resourceManager.getString(R.string.dapp_dapps)
            DappSearchGroup.SEARCH -> resourceManager.getString(R.string.common_search)
        }

        return TextHeader(content)
    }

    private fun mapSearchResultToSearchModel(searchResult: DappSearchResult): DappSearchModel {
        return when (searchResult) {
            is DappSearchResult.Dapp -> DappSearchModel(
                title = searchResult.dapp.name,
                description = searchResult.dapp.description,
                icon = searchResult.dapp.iconLink,
                searchResult = searchResult,
                actionIcon = R.drawable.ic_heart_filled.takeIf { searchResult.dapp.isFavourite }
            )

            is DappSearchResult.Search -> DappSearchModel(
                title = searchResult.query,
                searchResult = searchResult,
                actionIcon = null
            )

            is DappSearchResult.Url -> DappSearchModel(
                title = searchResult.url,
                searchResult = searchResult,
                actionIcon = null
            )
        }
    }

    fun searchResultClicked(searchResult: DappSearchResult) {
        val newUrl = when (searchResult) {
            is DappSearchResult.Dapp -> searchResult.dapp.url
            is DappSearchResult.Search -> searchResult.searchUrl
            is DappSearchResult.Url -> searchResult.url
        }

        if (shouldReportResult()) {
            dAppSearchResponder.respond(DAppSearchCommunicator.Response(newUrl))
            router.back()
        } else {
            router.openDAppBrowser(newUrl)
        }
    }

    private fun shouldReportResult() = payload.initialUrl != null
}
