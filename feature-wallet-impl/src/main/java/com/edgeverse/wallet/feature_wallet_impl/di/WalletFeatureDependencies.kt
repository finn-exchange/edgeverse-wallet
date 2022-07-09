package com.edgeverse.wallet.feature_wallet_impl.di

import android.content.ContentResolver
import coil.ImageLoader
import com.google.gson.Gson
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.data.storage.encrypt.EncryptedPreferences
import com.edgeverse.wallet.common.interfaces.FileProvider
import com.edgeverse.wallet.common.resources.ClipboardManager
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.QrCodeGenerator
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.core_db.dao.AssetDao
import com.edgeverse.wallet.core_db.dao.OperationDao
import com.edgeverse.wallet.core_db.dao.PhishingAddressDao
import com.edgeverse.wallet.core_db.dao.TokenDao
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.runtime.di.LOCAL_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.edgeverse.wallet.runtime.network.rpc.RpcCalls
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger
import javax.inject.Named

interface WalletFeatureDependencies {

    fun preferences(): Preferences

    fun encryptedPreferences(): EncryptedPreferences

    fun resourceManager(): ResourceManager

    fun iconGenerator(): IconGenerator

    fun clipboardManager(): ClipboardManager

    fun contentResolver(): ContentResolver

    fun accountRepository(): AccountRepository

    fun assetsDao(): AssetDao

    fun tokenDao(): TokenDao

    fun operationDao(): OperationDao

    fun networkCreator(): NetworkApiCreator

    fun signer(): Signer

    fun logger(): Logger

    fun jsonMapper(): Gson

    fun addressIconGenerator(): AddressIconGenerator

    fun appLinksProvider(): AppLinksProvider

    fun qrCodeGenerator(): QrCodeGenerator

    fun fileProvider(): FileProvider

    fun externalAccountActions(): ExternalActions.Presentation

    fun httpExceptionHandler(): HttpExceptionHandler

    fun phishingAddressesDao(): PhishingAddressDao

    fun rpcCalls(): RpcCalls

    fun accountUpdateScope(): AccountUpdateScope

    fun addressDisplayUseCase(): AddressDisplayUseCase

    fun chainRegistry(): ChainRegistry

    @Named(REMOTE_STORAGE_SOURCE)
    fun remoteStorageSource(): StorageDataSource

    @Named(LOCAL_STORAGE_SOURCE)
    fun localStorageSource(): StorageDataSource

    fun extrinsicService(): ExtrinsicService

    fun imageLoader(): ImageLoader

    fun selectedAccountUseCase(): SelectedAccountUseCase

    fun validationExecutor(): ValidationExecutor

    fun eventsRepository(): EventsRepository
}
