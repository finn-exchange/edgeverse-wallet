package com.edgeverse.wallet.common.di.modules

import android.content.Context
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.google.gson.Gson
import com.neovisionaries.ws.client.WebSocketFactory
import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.BuildConfig
import com.edgeverse.wallet.common.data.network.*
import com.edgeverse.wallet.common.data.network.rpc.SocketSingleRequestExecutor
import com.edgeverse.wallet.common.mixin.api.NetworkStateMixin
import com.edgeverse.wallet.common.mixin.impl.NetworkStateProvider
import com.edgeverse.wallet.common.resources.ResourceManager
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger
import jp.co.soramitsu.fearless_utils.wsrpc.recovery.Reconnector
import jp.co.soramitsu.fearless_utils.wsrpc.request.RequestExecutor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

private const val HTTP_CACHE = "http_cache"
private const val CACHE_SIZE = 50L * 1024L * 1024L // 50 MiB
private const val TIMEOUT_SECONDS = 20L

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideAppLinksProvider(): AppLinksProvider {
        return AppLinksProvider(
            termsUrl = BuildConfig.TERMS_URL,
            privacyUrl = BuildConfig.PRIVACY_URL,
            payoutsLearnMore = BuildConfig.PAYOUTS_LEARN_MORE,
            setControllerLearnMore = BuildConfig.SET_CONTROLLER_LEARN_MORE,
            website = BuildConfig.WEBSITE_URL,
            github = BuildConfig.GITHUB_URL
        )
    }

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(
        context: Context
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .cache(Cache(File(context.cacheDir, HTTP_CACHE), CACHE_SIZE))
            .retryOnConnectionFailure(true)
            .addInterceptor(TimeHeaderInterceptor())

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return builder.build()
    }

    @Provides
    @ApplicationScope
    fun provideLogger(): Logger = AndroidLogger()

    @Provides
    @ApplicationScope
    fun provideApiCreator(
        okHttpClient: OkHttpClient
    ): NetworkApiCreator {
        return NetworkApiCreator(okHttpClient, "https://placeholder.com")
    }

    @Provides
    @ApplicationScope
    fun httpExceptionHandler(
        resourceManager: ResourceManager
    ): HttpExceptionHandler = HttpExceptionHandler(resourceManager)

    @Provides
    @ApplicationScope
    fun provideSocketFactory() = WebSocketFactory()

    @Provides
    fun provideReconnector() = Reconnector()

    @Provides
    fun provideRequestExecutor() = RequestExecutor()

    @Provides
    fun provideSocketService(
        mapper: Gson,
        socketFactory: WebSocketFactory,
        logger: Logger,
        reconnector: Reconnector,
        requestExecutor: RequestExecutor
    ): SocketService = SocketService(mapper, logger, socketFactory, reconnector, requestExecutor)

    @Provides
    @ApplicationScope
    fun provideSocketSingleRequestExecutor(
        mapper: Gson,
        logger: Logger,
        socketFactory: WebSocketFactory,
        resourceManager: ResourceManager
    ) = SocketSingleRequestExecutor(mapper, logger, socketFactory, resourceManager)

    @Provides
    fun provideNetworkStateMixin(): NetworkStateMixin = NetworkStateProvider()

    @Provides
    @ApplicationScope
    fun provideJsonMapper() = Gson()
}
