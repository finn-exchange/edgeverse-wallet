package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.di

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
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.di.validations.Select
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.CrowdloanContributeInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.validations.ContributeValidation
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.CrowdloanContributeViewModel
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import com.edgeverse.wallet.feature_wallet_api.domain.AssetUseCase
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class CrowdloanContributeModule {

    @Provides
    @IntoMap
    @ViewModelKey(CrowdloanContributeViewModel::class)
    fun provideViewModel(
        interactor: CrowdloanContributeInteractor,
        router: CrowdloanRouter,
        resourceManager: ResourceManager,
        assetUseCase: AssetUseCase,
        validationExecutor: ValidationExecutor,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        payload: ContributePayload,
        @Select contributeValidations: @JvmSuppressWildcards Set<ContributeValidation>,
        customContributeManager: CustomContributeManager,
    ): ViewModel {
        return CrowdloanContributeViewModel(
            router,
            interactor,
            resourceManager,
            assetUseCase,
            validationExecutor,
            feeLoaderMixin,
            payload,
            contributeValidations,
            customContributeManager
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): CrowdloanContributeViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CrowdloanContributeViewModel::class.java)
    }
}
