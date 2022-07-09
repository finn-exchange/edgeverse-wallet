package com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.set.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.controller.ControllerInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.controller.SetControllerValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.set.SetControllerViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class SetControllerModule {
    @Provides
    @IntoMap
    @ViewModelKey(SetControllerViewModel::class)
    fun provideViewModel(
        interactor: ControllerInteractor,
        stackingInteractor: StakingInteractor,
        addressIconGenerator: AddressIconGenerator,
        router: StakingRouter,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        externalActions: ExternalActions.Presentation,
        appLinksProvider: AppLinksProvider,
        resourceManager: ResourceManager,
        addressDisplayUseCase: AddressDisplayUseCase,
        validationExecutor: ValidationExecutor,
        validationSystem: SetControllerValidationSystem,
        selectedAssetState: StakingSharedState,
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory
    ): ViewModel {
        return SetControllerViewModel(
            interactor = interactor,
            stakingInteractor = stackingInteractor,
            addressIconGenerator = addressIconGenerator,
            router = router,
            feeLoaderMixin = feeLoaderMixin,
            externalActions = externalActions,
            appLinksProvider = appLinksProvider,
            resourceManager = resourceManager,
            addressDisplayUseCase = addressDisplayUseCase,
            validationExecutor = validationExecutor,
            validationSystem = validationSystem,
            selectedAssetState = selectedAssetState,
            actionAwaitableMixinFactory = actionAwaitableMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SetControllerViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SetControllerViewModel::class.java)
    }
}
