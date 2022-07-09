package com.edgeverse.wallet.di.app.navigation

import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.root.navigation.dApp.DAppNavigator
import com.edgeverse.wallet.root.navigation.dApp.DAppSearchCommunicatorImpl
import com.edgeverse.wallet.root.navigation.dApp.DAppSignCommunicatorImpl
import dagger.Module
import dagger.Provides

@Module
class DAppNavigationModule {

    @ApplicationScope
    @Provides
    fun provideRouter(navigationHolder: NavigationHolder): DAppRouter = DAppNavigator(navigationHolder)

    @ApplicationScope
    @Provides
    fun provideSignExtrinsicCommunicator(navigationHolder: NavigationHolder): DAppSignCommunicator {
        return DAppSignCommunicatorImpl(navigationHolder)
    }

    @ApplicationScope
    @Provides
    fun provideSearchDappCommunicator(navigationHolder: NavigationHolder): DAppSearchCommunicator {
        return DAppSearchCommunicatorImpl(navigationHolder)
    }
}
