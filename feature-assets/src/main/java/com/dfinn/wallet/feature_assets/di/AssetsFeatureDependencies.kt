package com.dfinn.wallet.feature_assets.di

import android.content.ContentResolver
import coil.ImageLoader
import com.google.gson.Gson
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.memory.ComputationalCache
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.common.data.storage.encrypt.EncryptedPreferences
import com.dfinn.wallet.common.interfaces.FileProvider
import com.dfinn.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.dfinn.wallet.common.resources.ClipboardManager
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.QrCodeGenerator
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_account_api.presenatation.mixin.addressInput.AddressInputMixinFactory
import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.dfinn.wallet.runtime.di.LOCAL_STORAGE_SOURCE
import com.dfinn.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.dfinn.wallet.runtime.multiNetwork.runtime.repository.EventsRepository
import com.dfinn.wallet.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger
import javax.inject.Named

interface AssetsFeatureDependencies {

    fun preferences(): Preferences

    fun encryptedPreferences(): EncryptedPreferences

    fun resourceManager(): ResourceManager

    fun iconGenerator(): IconGenerator

    fun clipboardManager(): ClipboardManager

    fun contentResolver(): ContentResolver

    fun accountRepository(): AccountRepository

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

    fun walletRepository(): WalletRepository

    fun feeLoaderMixinFactory(): FeeLoaderMixin.Factory

    fun amountChooserFactory(): AmountChooserMixin.Factory

    fun walletConstants(): WalletConstants

    val assetsSourceRegistry: AssetSourceRegistry

    fun nftRepository(): NftRepository

    val addressInputMixinFactory: AddressInputMixinFactory

    val multiChainQrSharingFactory: MultiChainQrSharingFactory

    val walletUiUseCase: WalletUiUseCase

    val computationalCache: ComputationalCache

    val actionAwaitableMixinFactory: ActionAwaitableMixin.Factory
}
