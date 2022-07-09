package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.feature_dapp_impl.DAppRouter
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.extrinsicDetails.DAppExtrinsicDetailsViewModel

@Module(includes = [ViewModelModule::class])
class DAppExtrinsicDetailsModule {

    @Provides
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): DAppExtrinsicDetailsViewModel {
        return ViewModelProvider(fragment, factory).get(DAppExtrinsicDetailsViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DAppExtrinsicDetailsViewModel::class)
    fun provideViewModel(
        router: DAppRouter,
        extrinsicContent: String
    ): ViewModel {
        return DAppExtrinsicDetailsViewModel(
            router = router,
            extrinsicContent = extrinsicContent
        )
    }
}
