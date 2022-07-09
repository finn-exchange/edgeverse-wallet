package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.data.repository.FavouritesDAppRepository
import com.edgeverse.wallet.feature_dapp_impl.data.repository.PhishingSitesRepository
import com.edgeverse.wallet.feature_dapp_impl.domain.browser.DappBrowserInteractor
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main.DAppBrowserViewModel
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.edgeverse.wallet.feature_dapp_impl.presentation.search.DAppSearchCommunicator
import com.edgeverse.wallet.feature_dapp_impl.web3.states.ExtensionStoreFactory

@Module(includes = [ViewModelModule::class])
class DAppBrowserModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        phishingSitesRepository: PhishingSitesRepository,
        favouritesDAppRepository: FavouritesDAppRepository,
    ) = DappBrowserInteractor(
        phishingSitesRepository = phishingSitesRepository,
        favouritesDAppRepository = favouritesDAppRepository
    )

    @Provides
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): DAppBrowserViewModel {
        return ViewModelProvider(fragment, factory).get(DAppBrowserViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DAppBrowserViewModel::class)
    fun provideViewModel(
        router: DAppRouter,
        interactor: DappBrowserInteractor,
        selectedAccountUseCase: SelectedAccountUseCase,
        signRequester: DAppSignCommunicator,
        searchRequester: DAppSearchCommunicator,
        initialUrl: String,
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory,
        extensionStoreFactory: ExtensionStoreFactory,
    ): ViewModel {
        return DAppBrowserViewModel(
            router = router,
            interactor = interactor,
            selectedAccountUseCase = selectedAccountUseCase,
            signRequester = signRequester,
            dAppSearchRequester = searchRequester,
            initialUrl = initialUrl,
            actionAwaitableMixinFactory = actionAwaitableMixinFactory,
            extensionStoreFactory = extensionStoreFactory
        )
    }
}
