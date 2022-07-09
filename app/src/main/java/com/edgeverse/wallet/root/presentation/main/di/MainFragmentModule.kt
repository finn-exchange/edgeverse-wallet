package com.edgeverse.wallet.root.presentation.main.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.root.domain.RootInteractor
import com.edgeverse.wallet.root.presentation.main.MainViewModel
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
class MainFragmentModule {

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideViewModel(
        externalRequirementsFlow: MutableStateFlow<ChainConnection.ExternalRequirement>,
        interactor: RootInteractor
    ): ViewModel {
        return MainViewModel(interactor, externalRequirementsFlow)
    }

    @Provides
    fun provideViewModelCreator(
        activity: FragmentActivity,
        viewModelFactory: ViewModelProvider.Factory
    ): MainViewModel {
        return ViewModelProvider(activity, viewModelFactory).get(MainViewModel::class.java)
    }
}
