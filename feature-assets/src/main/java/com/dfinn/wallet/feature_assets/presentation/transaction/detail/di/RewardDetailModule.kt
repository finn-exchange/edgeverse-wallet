package com.dfinn.wallet.feature_assets.presentation.transaction.detail.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.dfinn.wallet.feature_assets.presentation.transaction.detail.reward.RewardDetailViewModel
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

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
