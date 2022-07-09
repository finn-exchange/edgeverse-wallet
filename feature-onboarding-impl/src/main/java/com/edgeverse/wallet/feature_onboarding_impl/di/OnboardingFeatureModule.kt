package com.edgeverse.wallet.feature_onboarding_impl.di

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.feature_onboarding_api.domain.OnboardingInteractor
import com.edgeverse.wallet.feature_onboarding_impl.domain.OnboardingInteractorImpl

@Module
class OnboardingFeatureModule {

    @Provides
    fun provideOnboardingInteractor(): OnboardingInteractor {
        return OnboardingInteractorImpl()
    }
}
