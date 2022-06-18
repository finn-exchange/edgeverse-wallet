package com.dfinn.wallet.di.app.navigation

import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_onboarding_impl.OnboardingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.root.navigation.NavigationHolder
import com.dfinn.wallet.root.navigation.Navigator
import com.dfinn.wallet.root.presentation.RootRouter
import com.dfinn.wallet.splash.SplashRouter
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        AccountNavigationModule::class,
        DAppNavigationModule::class,
        NftNavigationModule::class,
    ]
)
class NavigationModule {

    @ApplicationScope
    @Provides
    fun provideNavigatorHolder(): NavigationHolder = NavigationHolder()

    @ApplicationScope
    @Provides
    fun provideNavigator(
        navigatorHolder: NavigationHolder
    ): Navigator = Navigator(navigatorHolder)

    @Provides
    @ApplicationScope
    fun provideRootRouter(navigator: Navigator): RootRouter = navigator

    @ApplicationScope
    @Provides
    fun provideSplashRouter(navigator: Navigator): SplashRouter = navigator

    @ApplicationScope
    @Provides
    fun provideOnboardingRouter(navigator: Navigator): OnboardingRouter = navigator

    @ApplicationScope
    @Provides
    fun provideWalletRouter(navigator: Navigator): WalletRouter = navigator

    @ApplicationScope
    @Provides
    fun provideStakingRouter(navigator: Navigator): StakingRouter = navigator

    @ApplicationScope
    @Provides
    fun provideCrowdloanRouter(navigator: Navigator): CrowdloanRouter = navigator
}
