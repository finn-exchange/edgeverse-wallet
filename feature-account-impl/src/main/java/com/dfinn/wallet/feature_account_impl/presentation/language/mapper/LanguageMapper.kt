package com.dfinn.wallet.feature_account_impl.presentation.language.mapper

import com.dfinn.wallet.core.model.Language
import com.dfinn.wallet.feature_account_impl.presentation.language.model.LanguageModel
import java.util.Locale

fun mapLanguageToLanguageModel(language: Language): LanguageModel {
    val languageLocale = Locale(language.iso)
    return LanguageModel(
        language.iso,
        languageLocale.displayLanguage.capitalize(),
        languageLocale.getDisplayLanguage(languageLocale).capitalize()
    )
}
