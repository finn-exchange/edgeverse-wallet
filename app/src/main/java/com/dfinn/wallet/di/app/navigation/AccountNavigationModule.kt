package com.dfinn.wallet.di.app.navigation

import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.dfinn.wallet.root.navigation.NavigationHolder
import com.dfinn.wallet.root.navigation.Navigator
import com.dfinn.wallet.root.navigation.account.AdvancedEncryptionCommunicatorImpl
import dagger.Module
import dagger.Provides

@Module
class AccountNavigationModule {

    @Provides
    @ApplicationScope
    fun provideAdvancedEncryptionCommunicator(
        navigationHolder: NavigationHolder
    ): AdvancedEncryptionCommunicator = AdvancedEncryptionCommunicatorImpl(navigationHolder)

    @ApplicationScope
    @Provides
    fun provideAccountRouter(navigator: Navigator): AccountRouter = navigator
}
