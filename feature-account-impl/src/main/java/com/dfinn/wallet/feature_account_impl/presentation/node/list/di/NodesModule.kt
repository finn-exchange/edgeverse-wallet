package com.dfinn.wallet.feature_account_impl.presentation.node.list.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.node.list.NodesViewModel
import com.dfinn.wallet.feature_account_impl.presentation.node.mixin.api.NodeListingMixin
import com.dfinn.wallet.feature_account_impl.presentation.node.mixin.impl.NodeListingProvider

@Module(includes = [ViewModelModule::class])
class NodesModule {

    @Provides
    fun provideNodeListingMixin(
        interactor: AccountInteractor,
        resourceManager: ResourceManager
    ): NodeListingMixin = NodeListingProvider(interactor, resourceManager)

    @Provides
    @IntoMap
    @ViewModelKey(NodesViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        nodeListingMixin: NodeListingMixin,
        addressIconGenerator: AddressIconGenerator,
        resourceManager: ResourceManager
    ): ViewModel {
        return NodesViewModel(interactor, router, nodeListingMixin, addressIconGenerator, resourceManager)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): NodesViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(NodesViewModel::class.java)
    }
}
