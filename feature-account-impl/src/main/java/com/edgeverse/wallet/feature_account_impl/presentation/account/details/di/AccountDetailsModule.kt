package com.edgeverse.wallet.feature_account_impl.presentation.account.details.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.modules.Caching
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.edgeverse.wallet.feature_account_impl.domain.account.details.AccountDetailsInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.account.details.AccountDetailsViewModel
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherMixin
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class AccountDetailsModule {

    @Provides
    @IntoMap
    @ViewModelKey(AccountDetailsViewModel::class)
    fun provideViewModel(
        interactor: AccountDetailsInteractor,
        router: AccountRouter,
        resourceManager: ResourceManager,
        @Caching iconGenerator: AddressIconGenerator,
        metaId: Long,
        externalActions: ExternalActions.Presentation,
        chainRegistry: ChainRegistry,
        importTypeChooserMixin: ImportTypeChooserMixin.Presentation,
        addAccountLauncherMixin: AddAccountLauncherMixin.Presentation,
    ): ViewModel {
        return AccountDetailsViewModel(
            interactor,
            router,
            iconGenerator,
            resourceManager,
            metaId,
            externalActions,
            chainRegistry,
            importTypeChooserMixin,
            addAccountLauncherMixin
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): AccountDetailsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(AccountDetailsViewModel::class.java)
    }
}
