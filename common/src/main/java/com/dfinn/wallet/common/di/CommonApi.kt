package com.dfinn.wallet.common.di

import android.content.ContentResolver
import android.content.Context
import coil.ImageLoader
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.memory.ComputationalCache
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.network.rpc.BulkRetriever
import com.dfinn.wallet.common.data.network.rpc.SocketSingleRequestExecutor
import com.dfinn.wallet.common.data.secrets.v1.SecretStoreV1
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.common.data.storage.encrypt.EncryptedPreferences
import com.dfinn.wallet.common.di.modules.Caching
import com.dfinn.wallet.common.interfaces.FileProvider
import com.dfinn.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.dfinn.wallet.common.mixin.api.CustomDialogDisplayer
import com.dfinn.wallet.common.mixin.api.NetworkStateMixin
import com.dfinn.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.dfinn.wallet.common.resources.*
import com.dfinn.wallet.common.utils.QrCodeGenerator
import com.dfinn.wallet.common.utils.systemCall.SystemCallExecutor
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.vibration.DeviceVibrator
import com.google.gson.Gson
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger
import okhttp3.OkHttpClient
import java.util.*

interface CommonApi {

    fun computationalCache(): ComputationalCache

    fun imageLoader(): ImageLoader

    fun context(): Context

    fun provideResourceManager(): ResourceManager

    fun provideNetworkApiCreator(): NetworkApiCreator

    fun provideAppLinksProvider(): AppLinksProvider

    fun providePreferences(): Preferences

    fun provideEncryptedPreferences(): EncryptedPreferences

    fun provideIconGenerator(): IconGenerator

    fun provideClipboardManager(): ClipboardManager

    fun provideDeviceVibrator(): DeviceVibrator

    fun signer(): Signer

    fun logger(): Logger

    fun contextManager(): ContextManager

    fun languagesHolder(): LanguagesHolder

    fun provideJsonMapper(): Gson

    fun socketServiceCreator(): SocketService

    fun provideSocketSingleRequestExecutor(): SocketSingleRequestExecutor

    fun addressIconGenerator(): AddressIconGenerator

    @Caching
    fun cachingAddressIconGenerator(): AddressIconGenerator

    fun networkStateMixin(): NetworkStateMixin

    fun qrCodeGenerator(): QrCodeGenerator

    fun fileProvider(): FileProvider

    fun random(): Random

    fun contentResolver(): ContentResolver

    fun httpExceptionHandler(): HttpExceptionHandler

    fun defaultPagedKeysRetriever(): BulkRetriever

    fun validationExecutor(): ValidationExecutor

    fun secretStoreV1(): SecretStoreV1

    fun secretStoreV2(): SecretStoreV2

    fun customDialogDisplayer(): CustomDialogDisplayer.Presentation

    fun appVersionsProvider(): AppVersionProvider

    val systemCallExecutor: SystemCallExecutor

    val actionAwaitableMixinFactory: ActionAwaitableMixin.Factory

    val resourcesHintsMixinFactory: ResourcesHintsMixinFactory

    val okHttpClient: OkHttpClient
}
