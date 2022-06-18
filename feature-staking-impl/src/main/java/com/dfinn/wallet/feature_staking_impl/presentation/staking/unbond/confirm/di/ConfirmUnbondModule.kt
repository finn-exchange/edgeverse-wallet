package com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.confirm.di

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
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationSystem
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondViewModel
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory

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
