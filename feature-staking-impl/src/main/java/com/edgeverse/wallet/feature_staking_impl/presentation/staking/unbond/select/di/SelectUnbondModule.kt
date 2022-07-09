package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.select.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.unbond.UnbondValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.select.SelectUnbondViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class SelectUnbondModule {

    @Provides
    @IntoMap
    @ViewModelKey(SelectUnbondViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        unbondInteractor: UnbondInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        validationSystem: UnbondValidationSystem,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        unbondHintsMixinFactory: UnbondHintsMixinFactory,
        amountChooserMixinFactory: AmountChooserMixin.Factory
    ): ViewModel {
        return SelectUnbondViewModel(
            router = router,
            interactor = interactor,
            unbondInteractor = unbondInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            validationSystem = validationSystem,
            feeLoaderMixin = feeLoaderMixin,
            unbondHintsMixinFactory = unbondHintsMixinFactory,
            amountChooserMixinFactory = amountChooserMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SelectUnbondViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SelectUnbondViewModel::class.java)
    }
}
