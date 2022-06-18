package com.dfinn.wallet.feature_dapp_impl.presentation.search

import android.os.Parcelable
import com.dfinn.wallet.common.navigation.InterScreenRequester
import com.dfinn.wallet.common.navigation.InterScreenResponder
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator.Response
import kotlinx.android.parcel.Parcelize

interface DAppSearchRequester : InterScreenRequester<SearchPayload, Response>

interface DAppSearchResponder : InterScreenResponder<SearchPayload, Response>

interface DAppSearchCommunicator : DAppSearchRequester, DAppSearchResponder {

    @Parcelize
    class Response(val newUrl: String?) : Parcelable
}
