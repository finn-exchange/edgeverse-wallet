package com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem.di

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
import com.dfinn.wallet.feature_staking_impl.domain.staking.redeem.RedeemInteractor
import com.dfinn.wallet.feature_staking_impl.domain.validations.reedeem.RedeemValidationSystem
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem.RedeemPayload
import com.dfinn.wallet.feature_staking_impl.presentation.staking.redeem.RedeemViewModel
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class RedeemModule {

    @Provides
    @IntoMap
    @ViewModelKey(RedeemViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        redeemInteractor: RedeemInteractor,
        resourceManager: ResourceManager,
        validationExecutor: ValidationExecutor,
        validationSystem: RedeemValidationSystem,
        iconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        payload: RedeemPayload,
        singleAssetSharedState: StakingSharedState,
        walletUiUseCase: WalletUiUseCase
    ): ViewModel {
        return RedeemViewModel(
            router = router,
            interactor = interactor,
            redeemInteractor = redeemInteractor,
            resourceManager = resourceManager,
            validationExecutor = validationExecutor,
            validationSystem = validationSystem,
            iconGenerator = iconGenerator,
            feeLoaderMixin = feeLoaderMixin,
            externalActions = externalActions,
            selectedAssetState = singleAssetSharedState,
            payload = payload,
            walletUiUseCase = walletUiUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): RedeemViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(RedeemViewModel::class.java)
    }
}
