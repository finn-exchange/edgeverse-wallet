package com.dfinn.wallet.di.deps

import com.dfinn.wallet.App
import com.dfinn.wallet.common.di.FeatureApiHolder
import com.dfinn.wallet.common.di.FeatureContainer
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.core_db.di.DbHolder
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_account_impl.di.AccountFeatureHolder
import com.dfinn.wallet.feature_assets.di.AssetsFeatureApi
import com.dfinn.wallet.feature_assets.di.AssetsFeatureHolder
import com.dfinn.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.dfinn.wallet.feature_crowdloan_impl.di.CrowdloanFeatureHolder
import com.dfinn.wallet.feature_dapp_api.di.DAppFeatureApi
import com.dfinn.wallet.feature_dapp_impl.di.DAppFeatureHolder
import com.dfinn.wallet.feature_nft_api.NftFeatureApi
import com.dfinn.wallet.feature_nft_impl.di.NftFeatureHolder
import com.dfinn.wallet.feature_onboarding_api.di.OnboardingFeatureApi
import com.dfinn.wallet.feature_onboarding_impl.di.OnboardingFeatureHolder
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureHolder
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.feature_wallet_impl.di.WalletFeatureHolder
import com.dfinn.wallet.root.di.RootApi
import com.dfinn.wallet.root.di.RootFeatureHolder
import com.dfinn.wallet.runtime.di.RuntimeApi
import com.dfinn.wallet.runtime.di.RuntimeHolder
import com.dfinn.wallet.splash.di.SplashFeatureApi
import com.dfinn.wallet.splash.di.SplashFeatureHolder
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
