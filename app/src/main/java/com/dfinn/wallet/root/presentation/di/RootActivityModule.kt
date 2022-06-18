package com.dfinn.wallet.root.presentation.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.mixin.api.NetworkStateMixin
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.root.domain.RootInteractor
import com.dfinn.wallet.root.presentation.RootRouter
import com.dfinn.wallet.root.presentation.RootViewModel
import com.dfinn.wallet.runtime.multiNetwork.connection.ChainConnection
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
