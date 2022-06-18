package com.dfinn.wallet.feature_account_impl.presentation.importing.source.model

import android.net.Uri
import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.utils.Event

typealias RequestCode = Int

interface FileRequester {
    val chooseJsonFileEvent: LiveData<Event<RequestCode>>

    fun fileChosen(uri: Uri)
}
