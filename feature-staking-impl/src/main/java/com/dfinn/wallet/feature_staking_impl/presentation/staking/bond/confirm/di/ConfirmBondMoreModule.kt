package com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_staking_impl.data.StakingSharedState
import com.dfinn.wallet.feature_staking_impl.domain.StakingInteractor
import com.dfinn.wallet.feature_staking_impl.domain.staking.bond.BondMoreInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.bond.BondMoreValidationSystem
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload
import com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMoreViewModel

@Module(includes = [ViewModelModule::class])
class ConfirmBondMoreModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmBondMoreViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        bondMoreInteractor: BondMoreInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        validationSystem: BondMoreValidationSystem,
        iconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        payload: ConfirmBondMorePayload,
        singleAssetSharedState: StakingSharedState,
        walletUiUseCase: WalletUiUseCase,
        hintsMixinFactory: ResourcesHintsMixinFactory,
    ): ViewModel {
        return ConfirmBondMoreViewModel(
            router = router,
            interactor = interactor,
            bondMoreInteractor = bondMoreInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            iconGenerator = iconGenerator,
            validationSystem = validationSystem,
            externalActions = externalActions,
            payload = payload,
            selectedAssetState = singleAssetSharedState,
            walletUiUseCase = walletUiUseCase,
            hintsMixinFactory = hintsMixinFactory
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ConfirmBondMoreViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmBondMoreViewModel::class.java)
    }
}
