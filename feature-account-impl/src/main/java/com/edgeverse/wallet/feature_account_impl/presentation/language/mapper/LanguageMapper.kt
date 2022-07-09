package com.edgeverse.wallet.feature_account_impl.presentation.language.mapper

import com.edgeverse.wallet.core.model.Language
import com.edgeverse.wallet.feature_account_impl.presentation.language.model.LanguageModel
import java.util.Locale

fun mapLanguageToLanguageModel(language: Language): LanguageModel {
    val languageLocale = Locale(language.iso)
    return LanguageModel(
        language.iso,
        languageLocale.displayLanguage.capitalize(),
        languageLocale.getDisplayLanguage(languageLocale).capitalize()
    )
}
