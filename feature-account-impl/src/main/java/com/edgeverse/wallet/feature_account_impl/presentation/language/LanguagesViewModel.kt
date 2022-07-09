package com.edgeverse.wallet.feature_account_impl.presentation.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.core.model.Language
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.language.mapper.mapLanguageToLanguageModel
import com.edgeverse.wallet.feature_account_impl.presentation.language.model.LanguageModel
import kotlinx.coroutines.launch

class LanguagesViewModel(
    private val interactor: AccountInteractor,
    private val router: AccountRouter
) : BaseViewModel() {

    val languagesModels = getLanguages()

    val selectedLanguageLiveData = liveData {
        val languages = interactor.getSelectedLanguage()
        val mapped = mapLanguageToLanguageModel(languages)

        emit(mapped)
    }

    private val _languageChangedEvent = MutableLiveData<Event<Unit>>()
    val languageChangedEvent: LiveData<Event<Unit>> = _languageChangedEvent

    fun backClicked() {
        router.back()
    }

    fun selectLanguageClicked(languageModel: LanguageModel) {
        viewModelScope.launch {
            interactor.changeSelectedLanguage(Language(languageModel.iso))

            _languageChangedEvent.value = Event(Unit)
        }
    }

    private fun getLanguages() = interactor.getLanguages().map(::mapLanguageToLanguageModel)
}
