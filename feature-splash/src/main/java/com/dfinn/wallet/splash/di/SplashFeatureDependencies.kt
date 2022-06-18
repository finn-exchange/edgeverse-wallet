package com.dfinn.wallet.splash.di

import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository

interface SplashFeatureDependencies {
    fun accountRepository(): AccountRepository
}
