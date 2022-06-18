package com.dfinn.wallet.feature_assets.presentation.balance.filters.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.feature_assets.data.repository.assetFilters.AssetFiltersRepository
import com.dfinn.wallet.feature_assets.domain.assets.filters.AssetFiltersInteractor
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.balance.filters.AssetFiltersViewModel

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
