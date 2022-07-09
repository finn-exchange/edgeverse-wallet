package com.edgeverse.wallet

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.FeatureContainer
import com.edgeverse.wallet.common.resources.ContextManager
import com.edgeverse.wallet.common.resources.LanguagesHolder
import com.edgeverse.wallet.di.app.AppComponent
import com.edgeverse.wallet.di.app.DaggerAppComponent
import com.edgeverse.wallet.di.deps.FeatureHolderManager
import javax.inject.Inject

open class App : Application(), FeatureContainer {

    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    private lateinit var appComponent: AppComponent

    private val languagesHolder: LanguagesHolder = LanguagesHolder()

    override fun attachBaseContext(base: Context) {
        val contextManager = ContextManager.getInstanceOrInit(base, languagesHolder)
        super.attachBaseContext(contextManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val contextManager = ContextManager.getInstanceOrInit(this, languagesHolder)
        contextManager.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        val contextManger = ContextManager.getInstanceOrInit(this, languagesHolder)

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .contextManager(contextManger)
            .build()

        appComponent.inject(this)
    }

    override fun <T> getFeature(key: Class<*>): T {
        return featureHolderManager.getFeature<T>(key)!!
    }

    override fun releaseFeature(key: Class<*>) {
        featureHolderManager.releaseFeature(key)
    }

    override fun commonApi(): CommonApi {
        return appComponent
    }
}
