package com.dfinn.wallet.feature_staking_impl.presentation.validators.details.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.validators.details.ValidatorDetailsViewModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.parcel.ValidatorDetailsParcelModel

@Module(includes = [ViewModelModule::class])
class ValidatorDetailsModule {

    @Provides
    @IntoMap
    @ViewModelKey(ValidatorDetailsViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        validator: ValidatorDetailsParcelModel,
        addressIconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        appLinksProvider: AppLinksProvider,
        resourceManager: ResourceManager,
        singleAssetSharedState: StakingSharedState,
    ): ViewModel {
        return ValidatorDetailsViewModel(
            interactor,
            router,
            validator,
            addressIconGenerator,
            externalActions,
            appLinksProvider,
            resourceManager,
            singleAssetSharedState
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ValidatorDetailsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ValidatorDetailsViewModel::class.java)
    }
}
