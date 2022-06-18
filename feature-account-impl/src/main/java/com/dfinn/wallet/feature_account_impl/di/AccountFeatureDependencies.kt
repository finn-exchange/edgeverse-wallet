package com.dfinn.wallet.feature_account_impl.di

import android.content.Context
import coil.ImageLoader
import com.google.gson.Gson
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.network.rpc.SocketSingleRequestExecutor
import com.dfinn.wallet.common.data.secrets.v1.SecretStoreV1
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.common.data.storage.encrypt.EncryptedPreferences
import com.dfinn.wallet.common.di.modules.Caching
import com.dfinn.wallet.common.resources.AppVersionProvider
import com.dfinn.wallet.common.resources.ClipboardManager
import com.dfinn.wallet.common.resources.LanguagesHolder
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.systemCall.SystemCallExecutor
import com.dfinn.wallet.common.validation.ValidationExecutor
import com.dfinn.wallet.common.vibration.DeviceVibrator
import com.dfinn.wallet.core_db.dao.AccountDao
import com.dfinn.wallet.core_db.dao.MetaAccountDao
import com.dfinn.wallet.core_db.dao.NodeDao
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.dfinn.wallet.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import java.util.Random

interface AccountFeatureDependencies {

    fun networkCreator(): NetworkApiCreator

    fun appLinksProvider(): AppLinksProvider

    fun preferences(): Preferences

    fun encryptedPreferences(): EncryptedPreferences

    fun resourceManager(): ResourceManager

    fun iconGenerator(): IconGenerator

    fun clipboardManager(): ClipboardManager

    fun context(): Context

    fun deviceVibrator(): DeviceVibrator

    fun userDao(): AccountDao

    fun nodeDao(): NodeDao

    fun languagesHolder(): LanguagesHolder

    fun socketSingleRequestExecutor(): SocketSingleRequestExecutor

    fun jsonMapper(): Gson

    fun addressIconGenerator(): AddressIconGenerator

    @Caching
    fun cachingIconGenerator(): AddressIconGenerator

    fun random(): Random

    fun secretStoreV1(): SecretStoreV1

    fun secretStoreV2(): SecretStoreV2

    fun metaAccountDao(): MetaAccountDao

    fun chainRegistry(): ChainRegistry

    fun extrinsicBuilderFactory(): ExtrinsicBuilderFactory

    fun rpcCalls(): RpcCalls

    fun imageLoader(): ImageLoader

    fun appVersionProvider(): AppVersionProvider

    fun validationExecutor(): ValidationExecutor

    val systemCallExecutor: SystemCallExecutor

    val multiChainQrSharingFactory: MultiChainQrSharingFactory
}
