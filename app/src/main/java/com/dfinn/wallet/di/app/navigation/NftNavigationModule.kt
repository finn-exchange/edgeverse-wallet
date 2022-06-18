package com.dfinn.wallet.di.app.navigation

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.feature_nft_impl.NftRouter
import com.dfinn.wallet.root.navigation.NavigationHolder
import com.dfinn.wallet.root.navigation.nft.NftNavigator

@Module
class NftNavigationModule {

    @ApplicationScope
    @Provides
    fun provideRouter(navigationHolder: NavigationHolder): NftRouter = NftNavigator(navigationHolder)
}
