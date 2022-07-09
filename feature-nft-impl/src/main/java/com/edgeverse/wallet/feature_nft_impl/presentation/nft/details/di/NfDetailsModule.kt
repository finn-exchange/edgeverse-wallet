package com.edgeverse.wallet.feature_nft_impl.presentation.nft.details.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.common.di.viewmodel.ViewModelKey
import com.edgeverse.wallet.common.di.viewmodel.ViewModelModule
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_nft_api.data.repository.NftRepository
import com.edgeverse.wallet.feature_nft_impl.NftRouter
import com.edgeverse.wallet.feature_nft_impl.domain.nft.details.NftDetailsInteractor
import com.edgeverse.wallet.feature_nft_impl.presentation.nft.details.NftDetailsViewModel
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository

@Module(includes = [ViewModelModule::class])
class NfDetailsModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        nftRepository: NftRepository,
        tokenRepository: TokenRepository
    ) = NftDetailsInteractor(
        tokenRepository = tokenRepository,
        nftRepository = nftRepository
    )

    @Provides
    internal fun provideViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): NftDetailsViewModel {
        return ViewModelProvider(fragment, factory).get(NftDetailsViewModel::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(NftDetailsViewModel::class)
    fun provideViewModel(
        router: NftRouter,
        resourceManager: ResourceManager,
        interactor: NftDetailsInteractor,
        nftIdentifier: String,
        accountExternalActions: ExternalActions.Presentation,
        addressIconGenerator: AddressIconGenerator,
        addressDisplayUseCase: AddressDisplayUseCase
    ): ViewModel {
        return NftDetailsViewModel(
            router = router,
            resourceManager = resourceManager,
            interactor = interactor,
            nftIdentifier = nftIdentifier,
            externalActionsDelegate = accountExternalActions,
            addressIconGenerator = addressIconGenerator,
            addressDisplayUseCase = addressDisplayUseCase,
        )
    }
}
