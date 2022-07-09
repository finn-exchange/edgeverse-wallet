package com.edgeverse.wallet.feature_dapp_impl.presentation.search

import android.os.Parcelable
import com.edgeverse.wallet.common.navigation.InterScreenRequester
import com.edgeverse.wallet.common.navigation.InterScreenResponder
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator.Response
import kotlinx.android.parcel.Parcelize

interface DAppSearchRequester : InterScreenRequester<SearchPayload, Response>

interface DAppSearchResponder : InterScreenResponder<SearchPayload, Response>

interface DAppSearchCommunicator : DAppSearchRequester, DAppSearchResponder {

    @Parcelize
    class Response(val newUrl: String?) : Parcelable
}
