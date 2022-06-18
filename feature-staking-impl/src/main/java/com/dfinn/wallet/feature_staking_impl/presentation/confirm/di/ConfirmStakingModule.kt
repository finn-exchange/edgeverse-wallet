package com.dfinn.wallet.feature_staking_impl.presentation.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.validation.ValidationSystem
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.setup.SetupStakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.setup.SetupStakingPayload
import com.dfinn.wallet.feature_staking_impl.domain.validations.setup.SetupStakingValidationFailure
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.dfinn.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import com.dfinn.wallet.feature_staking_impl.presentation.confirm.ConfirmStakingViewModel
import com.dfinn.wallet.feature_staking_impl.presentation.confirm.hints.ConfirmStakeHintsMixinFactory
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

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
