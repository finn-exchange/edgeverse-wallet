package com.edgeverse.wallet.root.presentation.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.api.NetworkStateMixin
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.root.domain.RootInteractor
import com.edgeverse.wallet.root.presentation.RootRouter
import com.edgeverse.wallet.root.presentation.RootViewModel
import com.edgeverse.wallet.runtime.multiNetwork.connection.ChainConnection
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.MutableStateFlow

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class RootActivityModule {

    @Provides
    @IntoMap
    @ViewModelKey(RootViewModel::class)
    fun provideViewModel(
        interactor: RootInteractor,
        rootRouter: RootRouter,
        resourceManager: ResourceManager,
        networkStateMixin: NetworkStateMixin,
        externalRequirementsFlow: MutableStateFlow<ChainConnection.ExternalRequirement>
    ): ViewModel {
        return RootViewModel(
            interactor,
            rootRouter,
            externalRequirementsFlow,
            resourceManager,
            networkStateMixin
        )
    }

    @Provides
    fun provideViewModelCreator(
        activity: AppCompatActivity,
        viewModelFactory: ViewModelProvider.Factory
    ): RootViewModel {
        return ViewModelProvider(activity, viewModelFactory).get(RootViewModel::class.java)
    }
}
