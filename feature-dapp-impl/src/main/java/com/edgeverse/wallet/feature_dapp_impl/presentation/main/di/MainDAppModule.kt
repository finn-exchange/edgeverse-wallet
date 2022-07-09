package com.edgeverse.wallet.feature_dapp_impl.presentation.main.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.domain.DappInteractor
import com.edgeverse.wallet.feature_dapp_impl.presentation.main.MainDAppViewModel

@Module(includes = [ViewModelModule::class])
class MainDAppModule {

    @Provides
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): MainDAppViewModel {
        return ViewModelProvider(fragment, factory).get(MainDAppViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MainDAppViewModel::class)
    fun provideViewModel(
        addressIconGenerator: AddressIconGenerator,
        selectedAccountUseCase: SelectedAccountUseCase,
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory,
        router: DAppRouter,
        dappInteractor: DappInteractor
    ): ViewModel {
        return MainDAppViewModel(
            router = router,
            addressIconGenerator = addressIconGenerator,
            selectedAccountUseCase = selectedAccountUseCase,
            actionAwaitableMixinFactory = actionAwaitableMixinFactory,
            dappInteractor = dappInteractor
        )
    }
}
