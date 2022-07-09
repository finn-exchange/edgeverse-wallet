package com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.vibration.DeviceVibrator
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_impl.BuildConfig
import com.edgeverse.wallet.feature_account_impl.domain.account.add.AddAccountInteractor
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicConfig
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicViewModel

@Module(includes = [ViewModelModule::class])
class ConfirmMnemonicModule {

    @Provides
    @ScreenScope
    fun provideConfig() = ConfirmMnemonicConfig(
        allowShowingSkip = BuildConfig.DEBUG
    )

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmMnemonicViewModel::class)
    fun provideViewModel(
        interactor: AccountInteractor,
        addAccountInteractor: AddAccountInteractor,
        router: AccountRouter,
        deviceVibrator: DeviceVibrator,
        resourceManager: ResourceManager,
        config: ConfirmMnemonicConfig,
        payload: ConfirmMnemonicPayload
    ): ViewModel {
        return ConfirmMnemonicViewModel(interactor, addAccountInteractor, router, deviceVibrator, resourceManager, config, payload)
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): ConfirmMnemonicViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(ConfirmMnemonicViewModel::class.java)
    }
}
