package com.edgeverse.wallet.feature_account_impl.presentation.exporting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.Event

abstract class ExportViewModel : BaseViewModel() {

    private val _exportEvent = MutableLiveData<Event<String>>()
    val exportEvent: LiveData<Event<String>> = _exportEvent

    protected fun exportText(text: String) {
        _exportEvent.value = Event(text)
    }
}
