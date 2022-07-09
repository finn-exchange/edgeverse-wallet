package com.edgeverse.wallet.feature_onboarding_impl.di

import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin

interface OnboardingFeatureDependencies {

    fun accountRepository(): AccountRepository

    fun resourceManager(): ResourceManager

    fun appLinksProvider(): AppLinksProvider

    fun importTypeChooserMixin(): ImportTypeChooserMixin.Presentation
}
