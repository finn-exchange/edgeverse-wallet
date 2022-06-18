package com.dfinn.wallet.feature_assets.presentation.balance.list.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_assets.domain.WalletInteractor
import com.dfinn.wallet.feature_assets.domain.assets.list.AssetsListInteractor
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.balance.list.BalanceListViewModel
import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository

@Module(includes = [ViewModelModule::class])
class BalanceListModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        accountRepository: AccountRepository,
        nftRepository: NftRepository
    ) = AssetsListInteractor(accountRepository, nftRepository)

    @Provides
    @IntoMap
    @ViewModelKey(BalanceListViewModel::class)
    fun provideViewModel(
        interactor: WalletInteractor,
        assetsListInteractor: AssetsListInteractor,
        router: WalletRouter,
        selectedAccountUseCase: SelectedAccountUseCase,
        addressIconGenerator: AddressIconGenerator,
    ): ViewModel {

        return BalanceListViewModel(
            interactor,
            assetsListInteractor,
            addressIconGenerator,
            selectedAccountUseCase,
            router
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): BalanceListViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(BalanceListViewModel::class.java)
    }
}
