package com.dfinn.wallet.feature_account_impl.presentation.node.add.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.dfinn.wallet.feature_account_impl.domain.NodeHostValidator
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.node.add.AddNodeViewModel

@Module(includes = [ViewModelModule::class])
class AddNodeModule {

    @Provides
    @IntoMap
    @ViewModelKey(AddNodeViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        nodeHostValidator: NodeHostValidator,
        resourceManager: ResourceManager
    ): ViewModel {
        return AddNodeViewModel(interactor, router, nodeHostValidator, resourceManager)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): AddNodeViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(AddNodeViewModel::class.java)
    }
}
