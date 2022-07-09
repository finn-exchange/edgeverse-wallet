package com.edgeverse.wallet.di.deps

import com.edgeverse.wallet.App
import com.edgeverse.wallet.common.di.FeatureApiHolder
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.core_db.di.DbHolder
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_account_impl.di.AccountFeatureHolder
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureHolder
import com.edgeverse.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.edgeverse.wallet.feature_crowdloan_impl.di.CrowdloanFeatureHolder
import com.edgeverse.wallet.feature_dapp_api.di.DAppFeatureApi
import com.edgeverse.wallet.feature_dapp_impl.di.DAppFeatureHolder
import com.edgeverse.wallet.feature_nft_api.NftFeatureApi
import com.edgeverse.wallet.feature_nft_impl.di.NftFeatureHolder
import com.edgeverse.wallet.feature_onboarding_api.di.OnboardingFeatureApi
import com.edgeverse.wallet.feature_onboarding_impl.di.OnboardingFeatureHolder
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureHolder
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.feature_wallet_impl.di.WalletFeatureHolder
import com.edgeverse.wallet.root.di.RootApi
import com.edgeverse.wallet.root.di.RootFeatureHolder
import com.edgeverse.wallet.runtime.di.RuntimeApi
import com.edgeverse.wallet.runtime.di.RuntimeHolder
import com.edgeverse.wallet.splash.di.SplashFeatureApi
import com.edgeverse.wallet.splash.di.SplashFeatureHolder
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface ComponentHolderModule {

    @ApplicationScope
    @Binds
    fun provideFeatureContainer(application: App): FeatureContainer

    @ApplicationScope
    @Binds
    @ClassKey(SplashFeatureApi::class)
    @IntoMap
    fun provideSplashFeatureHolder(splashFeatureHolder: SplashFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(DbApi::class)
    @IntoMap
    fun provideDbFeature(dbHolder: DbHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(OnboardingFeatureApi::class)
    @IntoMap
    fun provideOnboardingFeature(onboardingFeatureHolder: OnboardingFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(DAppFeatureApi::class)
    @IntoMap
    fun provideDAppFeature(dAppFeatureHolder: DAppFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(AccountFeatureApi::class)
    @IntoMap
    fun provideAccountFeature(accountFeatureHolder: AccountFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(AssetsFeatureApi::class)
    @IntoMap
    fun provideAssetsFeature(holder: AssetsFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(WalletFeatureApi::class)
    @IntoMap
    fun provideWalletFeature(walletFeatureHolder: WalletFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(RootApi::class)
    @IntoMap
    fun provideMainFeature(accountFeatureHolder: RootFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(StakingFeatureApi::class)
    @IntoMap
    fun provideStakingFeature(holder: StakingFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(RuntimeApi::class)
    @IntoMap
    fun provideRuntimeFeature(runtimeHolder: RuntimeHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(CrowdloanFeatureApi::class)
    @IntoMap
    fun provideCrowdloanFeature(holder: CrowdloanFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(NftFeatureApi::class)
    @IntoMap
    fun provideNftFeature(holder: NftFeatureHolder): FeatureApiHolder
}
