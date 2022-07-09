package com.edgeverse.wallet.di.app.navigation

import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_onboarding_impl.OnboardingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.root.navigation.Navigator
import com.edgeverse.wallet.root.presentation.RootRouter
import com.edgeverse.wallet.splash.SplashRouter
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
