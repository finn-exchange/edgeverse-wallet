package com.edgeverse.wallet.feature_assets.presentation.balance.filters.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_assets.data.repository.assetFilters.AssetFiltersRepository
import com.edgeverse.wallet.feature_assets.domain.assets.filters.AssetFiltersInteractor
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.balance.filters.AssetFiltersViewModel

@Module(includes = [ViewModelModule::class])
class AssetFiltersModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        assetFiltersRepository: AssetFiltersRepository
    ) = AssetFiltersInteractor(assetFiltersRepository)

    @Provides
    @IntoMap
    @ViewModelKey(AssetFiltersViewModel::class)
    fun provideViewModel(
        interactor: AssetFiltersInteractor,
        router: WalletRouter
    ): ViewModel {
        return AssetFiltersViewModel(
            interactor = interactor,
            router = router
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): AssetFiltersViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(AssetFiltersViewModel::class.java)
    }
}
