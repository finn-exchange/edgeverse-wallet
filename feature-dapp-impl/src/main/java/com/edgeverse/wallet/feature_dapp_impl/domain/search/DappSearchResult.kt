package com.edgeverse.wallet.feature_dapp_impl.domain.search

import com.edgeverse.wallet.feature_dapp_api.data.model.DApp

sealed class DappSearchResult {

    class Url(val url: String) : DappSearchResult()

    class Search(val query: String, val searchUrl: String) : DappSearchResult()

    class Dapp(val dapp: DApp) : DappSearchResult()
}
