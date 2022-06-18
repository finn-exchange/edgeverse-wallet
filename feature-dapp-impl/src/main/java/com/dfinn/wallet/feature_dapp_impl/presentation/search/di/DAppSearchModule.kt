package com.dfinn.wallet.feature_dapp_impl.presentation.search.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_dapp_api.data.repository.DAppMetadataRepository
import com.dfinn.wallet.feature_dapp_impl.DAppRouter
import com.dfinn.wallet.feature_dapp_impl.data.repository.FavouritesDAppRepository
import com.dfinn.wallet.feature_dapp_impl.domain.search.SearchDappInteractor
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator
import com.dfinn.wallet.feature_dapp_impl.presentation.search.DAppSearchViewModel
import com.dfinn.wallet.feature_dapp_impl.presentation.search.SearchPayload

@Module(includes = [ViewModelModule::class])
class DAppSearchModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        dAppMetadataRepository: DAppMetadataRepository,
        favouritesDAppRepository: FavouritesDAppRepository
    ) = SearchDappInteractor(dAppMetadataRepository, favouritesDAppRepository)

    @Provides
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): DAppSearchViewModel {
        return ViewModelProvider(fragment, factory).get(DAppSearchViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DAppSearchViewModel::class)
    fun provideViewModel(
        router: DAppRouter,
        resourceManager: ResourceManager,
        interactor: SearchDappInteractor,
        searchResponder: DAppSearchCommunicator,
        payload: SearchPayload
    ): ViewModel {
        return DAppSearchViewModel(
            router = router,
            resourceManager = resourceManager,
            interactor = interactor,
            dAppSearchResponder = searchResponder,
            payload = payload
        )
    }
}
