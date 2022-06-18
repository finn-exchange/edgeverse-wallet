package com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.staking.bond.BondMoreInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationSystem
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMorePayload
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMoreViewModel
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

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
