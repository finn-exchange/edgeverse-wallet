package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.di

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
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rewardDestination.ChangeRewardDestinationInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rewardDestination.RewardDestinationValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.ConfirmRewardDestinationViewModel
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.ConfirmRewardDestinationPayload

@Module(includes = [ViewModelModule::class])
class ConfirmRewardDestinationModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmRewardDestinationViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        addressIconGenerator: AddressIconGenerator,
        resourceManager: ResourceManager,
        validationSystem: RewardDestinationValidationSystem,
        validationExecutor: ValidationExecutor,
        rewardDestinationInteractor: ChangeRewardDestinationInteractor,
        externalActions: ExternalActions.Presentation,
        payload: ConfirmRewardDestinationPayload,
        singleAssetSharedState: StakingSharedState,
        walletUiUseCase: WalletUiUseCase,
    ): ViewModel {
        return ConfirmRewardDestinationViewModel(
            router = router,
            interactor = interactor,
            addressIconGenerator = addressIconGenerator,
            resourceManager = resourceManager,
            validationSystem = validationSystem,
            rewardDestinationInteractor = rewardDestinationInteractor,
            externalActions = externalActions,
            validationExecutor = validationExecutor,
            payload = payload,
            selectedAssetState = singleAssetSharedState,
            walletUiUseCase = walletUiUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ConfirmRewardDestinationViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmRewardDestinationViewModel::class.java)
    }
}
