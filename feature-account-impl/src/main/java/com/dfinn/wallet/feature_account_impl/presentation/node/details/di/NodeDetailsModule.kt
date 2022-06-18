package com.dfinn.wallet.feature_account_impl.presentation.node.details.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ClipboardManager
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.node.details.NodeDetailsViewModel

@Module(includes = [ViewModelModule::class])
class NodeDetailsModule {

    @Provides
    @IntoMap
    @ViewModelKey(NodeDetailsViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        router: AccountRouter,
        nodeId: Int,
        clipboardManager: ClipboardManager,
        resourceManager: ResourceManager
    ): ViewModel {
        return NodeDetailsViewModel(interactor, router, nodeId, clipboardManager, resourceManager)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): NodeDetailsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(NodeDetailsViewModel::class.java)
    }
}
