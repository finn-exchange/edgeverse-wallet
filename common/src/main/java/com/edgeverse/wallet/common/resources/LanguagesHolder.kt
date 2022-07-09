package com.edgeverse.wallet.common.resources

import com.edgeverse.wallet.core.model.Language
import javax.inject.Singleton

@Singleton
class LanguagesHolder {

    companion object {
        private val RUSSIAN = Language("ru")
        private val ENGLISH = Language("en")

        private val availableLanguages = mutableListOf(RUSSIAN, ENGLISH)
    }

    fun getEnglishLang(): Language {
        return ENGLISH
    }

    fun getLanguages(): List<Language> {
        return availableLanguages
    }
}
