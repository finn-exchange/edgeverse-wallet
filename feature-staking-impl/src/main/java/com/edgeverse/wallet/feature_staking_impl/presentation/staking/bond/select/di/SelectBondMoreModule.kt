package com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select.di

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
import com.edgeverse.wallet.feature_staking_impl.domain.staking.bond.BondMoreInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationSystem
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMorePayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMoreViewModel
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class SelectBondMoreModule {

    @Provides
    @IntoMap
    @ViewModelKey(SelectBondMoreViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        bondMoreInteractor: BondMoreInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        validationSystem: BondMoreValidationSystem,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        payload: SelectBondMorePayload,
        amountChooserMixinFactory: AmountChooserMixin.Factory,
        resourcesHintsMixinFactory: ResourcesHintsMixinFactory,
    ): ViewModel {
        return SelectBondMoreViewModel(
            router = router,
            interactor = interactor,
            bondMoreInteractor = bondMoreInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            validationSystem = validationSystem,
            feeLoaderMixin = feeLoaderMixin,
            payload = payload,
            amountChooserMixinFactory = amountChooserMixinFactory,
            hintsMixinFactory = resourcesHintsMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SelectBondMoreViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SelectBondMoreViewModel::class.java)
    }
}
