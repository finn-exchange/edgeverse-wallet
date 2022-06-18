package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm.di

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
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_crowdloan_impl.data.CrowdloanSharedState
import com.dfinn.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.dfinn.wallet.feature_crowdloan_impl.di.validations.Confirm
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.CrowdloanContributeInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidation
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm.ConfirmContributeViewModel
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.confirm.parcel.ConfirmContributePayload
import com.dfinn.wallet.feature_wallet_api.domain.AssetUseCase

@Module(includes = [ViewModelModule::class])
class ConfirmContributeModule {

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmContributeViewModel::class)
    fun provideViewModel(
        interactor: CrowdloanContributeInteractor,
        router: CrowdloanRouter,
        resourceManager: ResourceManager,
        assetUseCase: AssetUseCase,
        validationExecutor: ValidationExecutor,
        payload: ConfirmContributePayload,
        accountUseCase: SelectedAccountUseCase,
        addressIconGenerator: AddressIconGenerator,
        @Confirm contributeValidations: @JvmSuppressWildcards Set<ContributeValidation>,
        externalActions: ExternalActions.Presentation,
        customContributeManager: CustomContributeManager,
        singleAssetSharedState: CrowdloanSharedState,
    ): ViewModel {
        return ConfirmContributeViewModel(
            router,
            interactor,
            resourceManager,
            assetUseCase,
            accountUseCase,
            addressIconGenerator,
            validationExecutor,
            payload,
            contributeValidations,
            customContributeManager,
            externalActions,
            singleAssetSharedState
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): ConfirmContributeViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmContributeViewModel::class.java)
    }
}
