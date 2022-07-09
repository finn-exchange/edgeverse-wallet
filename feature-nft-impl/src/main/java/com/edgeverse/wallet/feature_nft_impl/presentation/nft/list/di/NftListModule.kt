package com.edgeverse.wallet.feature_nft_impl.presentation.nft.list.di

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
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_nft_api.data.repository.NftRepository
import com.edgeverse.wallet.feature_nft_impl.NftRouter
import com.edgeverse.wallet.feature_nft_impl.domain.nft.list.NftListInteractor
import com.edgeverse.wallet.feature_nft_impl.presentation.nft.list.NftListViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository

@Module(includes = [ViewModelModule::class])
class NftListModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        accountRepository: AccountRepository,
        nftRepository: NftRepository,
        tokenRepository: TokenRepository
    ) = NftListInteractor(
        accountRepository = accountRepository,
        tokenRepository = tokenRepository,
        nftRepository = nftRepository
    )

    @Provides
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): NftListViewModel {
        return ViewModelProvider(fragment, factory).get(NftListViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(NftListViewModel::class)
    fun provideViewModel(
        router: NftRouter,
        resourceManager: ResourceManager,
        interactor: NftListInteractor,
    ): ViewModel {
        return NftListViewModel(
            router = router,
            resourceManager = resourceManager,
            interactor = interactor,
        )
    }
}
