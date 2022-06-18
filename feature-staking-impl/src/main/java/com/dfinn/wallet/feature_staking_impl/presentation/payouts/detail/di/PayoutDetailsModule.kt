package com.dfinn.wallet.feature_staking_impl.presentation.payouts.detail.di

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
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.payouts.detail.PayoutDetailsViewModel
import com.dfinn.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable

@Module(includes = [ViewModelModule::class])
class PayoutDetailsModule {

    @Provides
    @IntoMap
    @ViewModelKey(PayoutDetailsViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        payout: PendingPayoutParcelable,
        addressIconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        resourceManager: ResourceManager,
        selectedAssetState: StakingSharedState
    ): ViewModel {
        return PayoutDetailsViewModel(
            interactor,
            router,
            payout,
            addressIconGenerator,
            externalActions,
            resourceManager,
            selectedAssetState
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): PayoutDetailsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(PayoutDetailsViewModel::class.java)
    }
}
