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
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.reward.RewardDetailViewModel
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class RewardDetailModule {

    @Provides
    @IntoMap
    @ViewModelKey(RewardDetailViewModel::class)
    fun provideViewModel(
        operation: OperationParcelizeModel.Reward,
        addressIconGenerator: AddressIconGenerator,
        addressDisplayUseCase: AddressDisplayUseCase,
        chainRegistry: ChainRegistry,
        externalActions: ExternalActions.Presentation,
        router: WalletRouter
    ): ViewModel {
        return RewardDetailViewModel(
            operation,
            addressIconGenerator,
            addressDisplayUseCase,
            router,
            chainRegistry,
            externalActions,
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): RewardDetailViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(RewardDetailViewModel::class.java)
    }
}
