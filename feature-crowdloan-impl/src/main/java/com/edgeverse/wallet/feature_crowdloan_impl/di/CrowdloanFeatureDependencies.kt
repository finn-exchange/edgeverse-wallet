package com.edgeverse.wallet.feature_crowdloan_impl.di

import coil.ImageLoader
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.runtime.di.LOCAL_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.repository.ChainStateRepository
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import com.google.gson.Gson
import javax.inject.Named

interface CrowdloanFeatureDependencies {

    fun selectedAccountUseCase(): SelectedAccountUseCase

    fun walletConstants(): WalletConstants

    fun storageCache(): StorageCache

    fun imageLoader(): ImageLoader

    fun accountRepository(): AccountRepository

    fun addressIconGenerator(): AddressIconGenerator

    fun appLinksProvider(): AppLinksProvider

    fun walletRepository(): WalletRepository

    fun tokenRepository(): TokenRepository

    fun resourceManager(): ResourceManager

    fun externalAccountActions(): ExternalActions.Presentation

    fun networkApiCreator(): NetworkApiCreator

    fun httpExceptionHandler(): HttpExceptionHandler

    fun gson(): Gson

    fun addressxDisplayUseCase(): AddressDisplayUseCase

    fun extrinsicService(): ExtrinsicService

    fun validationExecutor(): ValidationExecutor

    @Named(REMOTE_STORAGE_SOURCE)
    fun remoteStorageSource(): StorageDataSource

    @Named(LOCAL_STORAGE_SOURCE)
    fun localStorageSource(): StorageDataSource

    fun chainStateRepository(): ChainStateRepository

    fun chainRegistry(): ChainRegistry

    fun preferences(): Preferences

    fun secretStoreV2(): SecretStoreV2

    fun customDialogDisplayer(): CustomDialogDisplayer.Presentation

    fun feeLoaderMixinFactory(): FeeLoaderMixin.Factory
}
