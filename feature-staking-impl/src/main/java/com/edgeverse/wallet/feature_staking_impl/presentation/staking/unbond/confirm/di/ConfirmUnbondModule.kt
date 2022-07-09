package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.di

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
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondViewModel
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory

@Module(includes = [ViewModelModule::class])
class ConfirmUnbondModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmUnbondViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        unbondInteractor: UnbondInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        validationSystem: UnbondValidationSystem,
        iconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        payload: ConfirmUnbondPayload,
        singleAssetSharedState: StakingSharedState,
        unbondHintsMixinFactory: UnbondHintsMixinFactory,
        walletUiUseCase: WalletUiUseCase,
    ): ViewModel {
        return ConfirmUnbondViewModel(
            router = router,
            interactor = interactor,
            unbondInteractor = unbondInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            iconGenerator = iconGenerator,
            validationSystem = validationSystem,
            externalActions = externalActions,
            payload = payload,
            selectedAssetState = singleAssetSharedState,
            unbondHintsMixinFactory = unbondHintsMixinFactory,
            walletUiUseCase = walletUiUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): ConfirmUnbondViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmUnbondViewModel::class.java)
    }
}
