package com.dfinn.wallet.feature_onboarding_impl.di

import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin

interface OnboardingFeatureDependencies {

    fun accountRepository(): AccountRepository

    fun resourceManager(): ResourceManager

    fun appLinksProvider(): AppLinksProvider

    fun importTypeChooserMixin(): ImportTypeChooserMixin.Presentation
}
