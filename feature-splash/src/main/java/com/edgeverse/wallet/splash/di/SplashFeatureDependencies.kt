package com.edgeverse.wallet.splash.di

import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository

interface SplashFeatureDependencies {
    fun accountRepository(): AccountRepository
}
