package com.dfinn.wallet.feature_dapp_impl.di

import coil.ImageLoader
import com.google.gson.Gson
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.core_db.dao.DappAuthorizationDao
import com.dfinn.wallet.core_db.dao.FavouriteDAppsDao
import com.dfinn.wallet.core_db.dao.PhishingSitesDao
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.dfinn.wallet.runtime.di.ExtrinsicSerialization
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
import okhttp3.OkHttpClient

interface DAppFeatureDependencies {

    fun accountRepository(): AccountRepository

    fun resourceManager(): ResourceManager

    fun appLinksProvider(): AppLinksProvider

    fun selectedAccountUseCase(): SelectedAccountUseCase

    fun addressIconGenerator(): AddressIconGenerator

    fun gson(): Gson

    fun chainRegistry(): ChainRegistry

    fun imageLoader(): ImageLoader

    fun feeLoaderMixinFactory(): FeeLoaderMixin.Factory

    fun extrinsicService(): ExtrinsicService

    fun tokenRepository(): TokenRepository

    fun secretStoreV2(): SecretStoreV2

    @ExtrinsicSerialization
    fun extrinsicGson(): Gson

    fun apiCreator(): NetworkApiCreator

    fun runtimeVersionsRepository(): RuntimeVersionsRepository

    fun dappAuthorizationDao(): DappAuthorizationDao

    val phishingSitesDao: PhishingSitesDao

    val favouriteDAppsDao: FavouriteDAppsDao

    val actionAwaitableMixinFactory: ActionAwaitableMixin.Factory

    val walletUiUseCase: WalletUiUseCase

    val okHttpClient: OkHttpClient

    val walletRepository: WalletRepository

    val validationExecutor: ValidationExecutor
}
