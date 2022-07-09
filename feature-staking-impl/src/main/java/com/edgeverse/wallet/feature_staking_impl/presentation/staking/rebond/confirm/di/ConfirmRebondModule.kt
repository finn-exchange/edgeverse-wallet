package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rebond.RebondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class ConfirmRebondModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmRebondViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        rebondInteractor: RebondInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        validationSystem: RebondValidationSystem,
        iconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        payload: ConfirmRebondPayload,
        singleAssetSharedState: StakingSharedState,
        hintsMixinFactory: ResourcesHintsMixinFactory,
        walletUiUseCase: WalletUiUseCase,
    ): ViewModel {
        return ConfirmRebondViewModel(
            router = router,
            interactor = interactor,
            rebondInteractor = rebondInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            validationSystem = validationSystem,
            iconGenerator = iconGenerator,
            externalActions = externalActions,
            feeLoaderMixin = feeLoaderMixin,
            payload = payload,
            selectedAssetState = singleAssetSharedState,
            hintsMixinFactory = hintsMixinFactory,
            walletUiUseCase = walletUiUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): ConfirmRebondViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmRebondViewModel::class.java)
    }
}
