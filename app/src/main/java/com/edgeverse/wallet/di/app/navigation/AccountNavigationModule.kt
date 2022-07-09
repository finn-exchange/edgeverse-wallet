package com.edgeverse.wallet.di.app.navigation

import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.root.navigation.Navigator
import com.edgeverse.wallet.root.navigation.account.AdvancedEncryptionCommunicatorImpl
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
