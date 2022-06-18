package com.dfinn.wallet.feature_assets.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.balance.detail.di.BalanceDetailComponent
import com.dfinn.wallet.feature_assets.presentation.balance.filters.di.AssetFiltersComponent
import com.dfinn.wallet.feature_assets.presentation.balance.list.di.BalanceListComponent
import com.dfinn.wallet.feature_assets.presentation.balance.list.view.GoToNftsView
import com.dfinn.wallet.feature_assets.presentation.receive.di.ReceiveComponent
import com.dfinn.wallet.feature_assets.presentation.send.amount.di.SelectSendComponent
import com.dfinn.wallet.feature_assets.presentation.send.confirm.di.ConfirmSendComponent
import com.dfinn.wallet.feature_assets.presentation.transaction.detail.di.ExtrinsicDetailComponent
import com.dfinn.wallet.feature_assets.presentation.transaction.detail.di.RewardDetailComponent
import com.dfinn.wallet.feature_assets.presentation.transaction.detail.di.TransactionDetailComponent
import com.dfinn.wallet.feature_assets.presentation.transaction.filter.di.TransactionHistoryFilterComponent
import com.dfinn.wallet.feature_nft_api.NftFeatureApi
import com.dfinn.wallet.feature_wallet_api.di.WalletFeatureApi
import com.dfinn.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        AssetsFeatureDependencies::class
    ],
    modules = [
        AssetsFeatureModule::class,
    ]
)
@FeatureScope
interface AssetsFeatureComponent : AssetsFeatureApi {

    fun balanceListComponentFactory(): BalanceListComponent.Factory

    fun balanceDetailComponentFactory(): BalanceDetailComponent.Factory

    fun chooseAmountComponentFactory(): SelectSendComponent.Factory

    fun confirmTransferComponentFactory(): ConfirmSendComponent.Factory

    fun transactionDetailComponentFactory(): TransactionDetailComponent.Factory

    fun transactionHistoryComponentFactory(): TransactionHistoryFilterComponent.Factory

    fun rewardDetailComponentFactory(): RewardDetailComponent.Factory

    fun extrinsicDetailComponentFactory(): ExtrinsicDetailComponent.Factory

    fun receiveComponentFactory(): ReceiveComponent.Factory

    fun assetFiltersComponentFactory(): AssetFiltersComponent.Factory

    fun inject(view: GoToNftsView)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance accountRouter: WalletRouter,
            deps: AssetsFeatureDependencies
        ): AssetsFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            RuntimeApi::class,
            NftFeatureApi::class,
            WalletFeatureApi::class,
            AccountFeatureApi::class
        ]
    )
    interface AssetsFeatureDependenciesComponent : AssetsFeatureDependencies
}
