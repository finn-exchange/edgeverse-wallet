package com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.di

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
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.payout.PayoutInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.MakePayoutPayload
import com.edgeverse.wallet.feature_staking_impl.domain.validations.payout.PayoutValidationFailure
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.ConfirmPayoutViewModel
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class ConfirmPayoutModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmPayoutViewModel::class)
    fun provideViewModel(
        interactor: StakingInteractor,
        router: StakingRouter,
        payload: ConfirmPayoutPayload,
        payoutInteractor: PayoutInteractor,
        addressIconGenerator: AddressIconGenerator,
        externalActions: ExternalActions.Presentation,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        validationSystem: ValidationSystem<MakePayoutPayload, PayoutValidationFailure>,
        validationExecutor: ValidationExecutor,
        resourceManager: ResourceManager,
        singleAssetSharedState: StakingSharedState,
        walletUiUseCase: WalletUiUseCase,
    ): ViewModel {
        return ConfirmPayoutViewModel(
            interactor = interactor,
            payoutInteractor = payoutInteractor,
            router = router,
            payload = payload,
            addressModelGenerator = addressIconGenerator,
            externalActions = externalActions,
            feeLoaderMixin = feeLoaderMixin,
            validationSystem = validationSystem,
            validationExecutor = validationExecutor,
            resourceManager = resourceManager,
            selectedAssetState = singleAssetSharedState,
            walletUiUseCase = walletUiUseCase
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): ConfirmPayoutViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmPayoutViewModel::class.java)
    }
}
