package com.edgeverse.wallet.feature_dapp_impl.di

import coil.ImageLoader
import com.google.gson.Gson
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.core_db.dao.DappAuthorizationDao
import com.edgeverse.wallet.core_db.dao.FavouriteDAppsDao
import com.edgeverse.wallet.core_db.dao.PhishingSitesDao
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.runtime.di.ExtrinsicSerialization
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
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
