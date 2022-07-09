package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.detail.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.detail.PayoutDetailsViewModel
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable

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
