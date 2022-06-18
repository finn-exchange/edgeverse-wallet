package com.dfinn.wallet.feature_account_impl.presentation.exporting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.utils.Event

abstract class ExportViewModel : BaseViewModel() {

    private val _exportEvent = MutableLiveData<Event<String>>()
    val exportEvent: LiveData<Event<String>> = _exportEvent

    protected fun exportText(text: String) {
        _exportEvent.value = Event(text)
    }
}
