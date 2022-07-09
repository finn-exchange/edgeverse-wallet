package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.custom.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.staking.rebond.RebondInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.rebond.RebondValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.custom.CustomRebondViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class CustomRebondModule {

    @Provides
    @IntoMap
    @ViewModelKey(CustomRebondViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        rebondInteractor: RebondInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        validationSystem: RebondValidationSystem,
        amountChooserMixin: AmountChooserMixin.Factory,
        hintsMixinFactory: ResourcesHintsMixinFactory,
    ): ViewModel {
        return CustomRebondViewModel(
            router = router,
            interactor = interactor,
            rebondInteractor = rebondInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            validationSystem = validationSystem,
            feeLoaderMixin = feeLoaderMixin,
            amountChooserMixinFactory = amountChooserMixin,
            hintsMixinFactory = hintsMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): CustomRebondViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CustomRebondViewModel::class.java)
    }
}
