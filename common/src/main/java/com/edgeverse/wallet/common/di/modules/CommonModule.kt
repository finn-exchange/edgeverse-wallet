package com.edgeverse.wallet.common.di.modules

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Vibrator
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.CachingAddressIconGenerator
import com.edgeverse.wallet.common.address.StatelessAddressIconGenerator
import com.edgeverse.wallet.common.data.FileProviderImpl
import com.edgeverse.wallet.common.data.memory.ComputationalCache
import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.data.secrets.v1.SecretStoreV1
import com.edgeverse.wallet.common.data.secrets.v1.SecretStoreV1Impl
import com.edgeverse.wallet.common.data.secrets.v2.SecretStoreV2
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.data.storage.PreferencesImpl
import com.edgeverse.wallet.common.data.storage.encrypt.EncryptedPreferences
import com.edgeverse.wallet.common.data.storage.encrypt.EncryptedPreferencesImpl
import com.edgeverse.wallet.common.data.storage.encrypt.EncryptionUtil
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.common.interfaces.FileProvider
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableProvider
import com.edgeverse.wallet.common.mixin.api.CustomDialogDisplayer
import com.edgeverse.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.edgeverse.wallet.common.mixin.impl.CustomDialogProvider
import com.edgeverse.wallet.common.resources.*
import com.edgeverse.wallet.common.utils.QrCodeGenerator
import com.edgeverse.wallet.common.utils.systemCall.SystemCallExecutor
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.common.vibration.DeviceVibrator
import dagger.Module
import dagger.Provides
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import java.security.SecureRandom
import java.util.*
import javax.inject.Qualifier

const val SHARED_PREFERENCES_FILE = "fearless_prefs"

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Caching

@Module
class CommonModule {

    @Provides
    @ApplicationScope
    fun provideComputationalCache() = ComputationalCache()

    @Provides
    @ApplicationScope
    fun imageLoader(context: Context) = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()

    @Provides
    @ApplicationScope
    fun provideResourceManager(contextManager: ContextManager): ResourceManager {
        return ResourceManagerImpl(contextManager)
    }

    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    @Provides
    @ApplicationScope
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return PreferencesImpl(sharedPreferences)
    }

    @Provides
    @ApplicationScope
    fun provideEncryptionUtil(context: Context): EncryptionUtil {
        return EncryptionUtil(context)
    }

    @Provides
    @ApplicationScope
    fun provideEncryptedPreferences(
        preferences: Preferences,
        encryptionUtil: EncryptionUtil,
    ): EncryptedPreferences {
        return EncryptedPreferencesImpl(preferences, encryptionUtil)
    }

    @Provides
    @ApplicationScope
    fun provideSigner(): Signer {
        return Signer
    }

    @Provides
    @ApplicationScope
    fun provideIconGenerator(): IconGenerator {
        return IconGenerator()
    }

    @Provides
    @ApplicationScope
    fun provideClipboardManager(context: Context): ClipboardManager {
        return ClipboardManager(context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager)
    }

    @Provides
    @ApplicationScope
    fun provideDeviceVibrator(context: Context): DeviceVibrator {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return DeviceVibrator(vibrator)
    }

    @Provides
    @ApplicationScope
    fun provideLanguagesHolder(): LanguagesHolder {
        return LanguagesHolder()
    }

    @Provides
    @ApplicationScope
    fun provideAddressModelCreator(
        resourceManager: ResourceManager,
        iconGenerator: IconGenerator,
    ): AddressIconGenerator = StatelessAddressIconGenerator(iconGenerator, resourceManager)

    @Provides
    @Caching
    fun provideCachingAddressModelCreator(
        delegate: AddressIconGenerator,
    ): AddressIconGenerator = CachingAddressIconGenerator(delegate)

    @Provides
    @ApplicationScope
    fun provideQrCodeGenerator(): QrCodeGenerator {
        return QrCodeGenerator(Color.BLACK, Color.WHITE)
    }

    @Provides
    @ApplicationScope
    fun provideFileProvider(contextManager: ContextManager): FileProvider {
        return FileProviderImpl(contextManager.getContext())
    }

    @Provides
    @ApplicationScope
    fun provideRandom(): Random = SecureRandom()

    @Provides
    @ApplicationScope
    fun provideContentResolver(
        context: Context,
    ): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @ApplicationScope
    fun provideDefaultPagedKeysRetriever(): BulkRetriever {
        return BulkRetriever()
    }

    @Provides
    @ApplicationScope
    fun provideValidationExecutor(): ValidationExecutor {
        return ValidationExecutor()
    }

    @Provides
    @ApplicationScope
    fun provideSecretStoreV1(
        encryptedPreferences: EncryptedPreferences,
    ): SecretStoreV1 = SecretStoreV1Impl(encryptedPreferences)

    @Provides
    @ApplicationScope
    fun provideSecretStoreV2(
        encryptedPreferences: EncryptedPreferences,
    ) = SecretStoreV2(encryptedPreferences)

    @Provides
    @ApplicationScope
    fun provideCustomDialogDisplayer(): CustomDialogDisplayer.Presentation = CustomDialogProvider()

    @Provides
    @ApplicationScope
    fun provideAppVersionsProvider(context: Context): AppVersionProvider {
        return OSAppVersionProvider(context)
    }

    @Provides
    @ApplicationScope
    fun provideSystemCallExecutor(): SystemCallExecutor = SystemCallExecutor()

    @Provides
    @ApplicationScope
    fun actionAwaitableMixinFactory(): ActionAwaitableMixin.Factory = ActionAwaitableProvider

    @Provides
    @ApplicationScope
    fun resourcesHintsMixinFactory(
        resourceManager: ResourceManager,
    ) = ResourcesHintsMixinFactory(resourceManager)
}
