package com.edgeverse.wallet.feature_staking_impl.presentation.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.setup.SetupStakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.ConfirmStakingViewModel
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.hints.ConfirmStakeHintsMixinFactory
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class ConfirmStakingModule {

    @Provides
    @ScreenScope
    fun provideConfirmStakeHintsMixinFactory(
        interactor: StakingInteractor,
        resourceManager: ResourceManager,
        stakingHintsUseCase: StakingHintsUseCase,
    ): ConfirmStakeHintsMixinFactory {
        return ConfirmStakeHintsMixinFactory(interactor, resourceManager, stakingHintsUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmStakingViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        addressIconGenerator: AddressIconGenerator,
        resourceManager: ResourceManager,
        addressDisplayUseCase: AddressDisplayUseCase,
        setupStakingInteractor: SetupStakingInteractor,
        validationSystem: ValidationSystem<SetupStakingPayload, SetupStakingValidationFailure>,
        validationExecutor: ValidationExecutor,
        setupStakingSharedState: SetupStakingSharedState,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        externalActions: ExternalActions.Presentation,
        singleAssetSharedState: StakingSharedState,
        walletUiUseCase: WalletUiUseCase,
        hintsMixinFactory: ConfirmStakeHintsMixinFactory,
    ): ViewModel {
        return ConfirmStakingViewModel(
            router = router,
            interactor = interactor,
            addressIconGenerator = addressIconGenerator,
            addressDisplayUseCase = addressDisplayUseCase,
            resourceManager = resourceManager,
            validationSystem = validationSystem,
            setupStakingSharedState = setupStakingSharedState,
            setupStakingInteractor = setupStakingInteractor,
            feeLoaderMixin = feeLoaderMixin,
            externalActions = externalActions,
            selectedAssetState = singleAssetSharedState,
            validationExecutor = validationExecutor,
            walletUiUseCase = walletUiUseCase,
            hintsMixinFactory = hintsMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): ConfirmStakingViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmStakingViewModel::class.java)
    }
}
