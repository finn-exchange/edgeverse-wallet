package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.validations.custom.moonbeam.MoonbeamTermsValidationSystem
import com.dfinn.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.MoonbeamCrowdloanTermsViewModel
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import com.dfinn.wallet.feature_wallet_api.domain.AssetUseCase
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin

@Module(includes = [ViewModelModule::class])
class MoonbeamCrowdloanTermsModule {

    @Provides
    @IntoMap
    @ViewModelKey(MoonbeamCrowdloanTermsViewModel::class)
    fun provideViewModel(
        interactor: MoonbeamCrowdloanInteractor,
        payload: ContributePayload,
        feeLoaderMixin: FeeLoaderMixin.Presentation,
        resourceManager: ResourceManager,
        router: CrowdloanRouter,
        assetUseCase: AssetUseCase,
        validationSystem: MoonbeamTermsValidationSystem,
        validationExecutor: ValidationExecutor,
    ): ViewModel {
        return MoonbeamCrowdloanTermsViewModel(
            interactor,
            payload,
            feeLoaderMixin,
            resourceManager,
            router,
            assetUseCase,
            validationExecutor,
            validationSystem
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): MoonbeamCrowdloanTermsViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(MoonbeamCrowdloanTermsViewModel::class.java)
    }
}
