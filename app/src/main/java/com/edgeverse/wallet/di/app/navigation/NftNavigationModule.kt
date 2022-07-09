package com.edgeverse.wallet.di.app.navigation

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.feature_nft_impl.NftRouter
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.root.navigation.nft.NftNavigator

@Module
class NftNavigationModule {

    @ApplicationScope
    @Provides
    fun provideRouter(navigationHolder: NavigationHolder): NftRouter = NftNavigator(navigationHolder)
}
