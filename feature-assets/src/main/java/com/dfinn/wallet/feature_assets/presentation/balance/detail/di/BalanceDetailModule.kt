package com.dfinn.wallet.feature_assets.presentation.balance.detail.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.common.di.viewmodel.ViewModelKey
import com.dfinn.wallet.common.di.viewmodel.ViewModelModule
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_assets.domain.WalletInteractor
import com.dfinn.wallet.feature_assets.domain.send.SendInteractor
import com.dfinn.wallet.feature_assets.presentation.AssetPayload
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.balance.assetActions.buy.BuyMixinFactory
import com.dfinn.wallet.feature_assets.presentation.balance.detail.BalanceDetailViewModel
import com.dfinn.wallet.feature_assets.presentation.transaction.filter.HistoryFiltersProviderFactory
import com.dfinn.wallet.feature_assets.presentation.transaction.history.mixin.TransactionHistoryMixin
import com.dfinn.wallet.feature_assets.presentation.transaction.history.mixin.TransactionHistoryProvider
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class BalanceDetailModule {

    @Provides
    @ScreenScope
    fun provideTransferHistoryMixin(
        walletInteractor: WalletInteractor,
        walletRouter: WalletRouter,
        historyFiltersProviderFactory: HistoryFiltersProviderFactory,
        assetSourceRegistry: AssetSourceRegistry,
        resourceManager: ResourceManager,
        assetPayload: AssetPayload,
        addressDisplayUseCase: AddressDisplayUseCase,
        chainRegistry: ChainRegistry,
    ): TransactionHistoryMixin {
        return TransactionHistoryProvider(
            walletInteractor = walletInteractor,
            router = walletRouter,
            historyFiltersProviderFactory = historyFiltersProviderFactory,
            resourceManager = resourceManager,
            addressDisplayUseCase = addressDisplayUseCase,
            assetsSourceRegistry = assetSourceRegistry,
            chainRegistry = chainRegistry,
            chainId = assetPayload.chainId,
            assetId = assetPayload.chainAssetId
        )
    }

    @Provides
    @IntoMap
    @ViewModelKey(BalanceDetailViewModel::class)
    fun provideViewModel(
        interactor: WalletInteractor,
        sendInteractor: SendInteractor,
        router: WalletRouter,
        transactionHistoryMixin: TransactionHistoryMixin,
        buyMixinFactory: BuyMixinFactory,
        assetPayload: AssetPayload
    ): ViewModel {
        return BalanceDetailViewModel(
            interactor = interactor,
            sendInteractor = sendInteractor,
            router = router,
            assetPayload = assetPayload,
            buyMixinFactory = buyMixinFactory,
            transactionHistoryMixin = transactionHistoryMixin
        )
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): BalanceDetailViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(BalanceDetailViewModel::class.java)
    }
}
