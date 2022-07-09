package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.transfer.TransactionDetailViewModel
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class TransactionDetailModule {

    @Provides
    @IntoMap
    @ViewModelKey(TransactionDetailViewModel::class)
    fun provideViewModel(
        router: WalletRouter,
        addressIconGenerator: AddressIconGenerator,
        addressDisplayUseCase: AddressDisplayUseCase,
        chainRegistry: ChainRegistry,
        operation: OperationParcelizeModel.Transfer,
        externalActions: ExternalActions.Presentation
    ): ViewModel {
        return TransactionDetailViewModel(
            router,
            addressIconGenerator,
            addressDisplayUseCase,
            chainRegistry,
            operation,
            externalActions
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): TransactionDetailViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(TransactionDetailViewModel::class.java)
    }
}
