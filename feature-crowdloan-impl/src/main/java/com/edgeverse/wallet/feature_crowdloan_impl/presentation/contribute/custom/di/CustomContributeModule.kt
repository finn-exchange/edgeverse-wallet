package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_crowdloan_impl.di.customCrowdloan.CustomContributeManager
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeViewModel
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload

@Module(includes = [ViewModelModule::class])
class CustomContributeModule {

    @Provides
    @IntoMap
    @ViewModelKey(CustomContributeViewModel::class)
    fun provideViewModel(
        customContributeManager: CustomContributeManager,
        payload: CustomContributePayload,
        router: CrowdloanRouter,
    ): ViewModel {
        return CustomContributeViewModel(customContributeManager, payload, router)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): CustomContributeViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(CustomContributeViewModel::class.java)
    }
}
