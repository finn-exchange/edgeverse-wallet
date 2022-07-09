package com.edgeverse.wallet.di.app

import com.edgeverse.wallet.App
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.modules.CommonModule
import com.edgeverse.wallet.common.di.modules.NetworkModule
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.common.resources.ContextManager
import com.edgeverse.wallet.di.app.navigation.NavigationModule
import com.edgeverse.wallet.di.deps.ComponentHolderModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        CommonModule::class,
        NetworkModule::class,
        NavigationModule::class,
        ComponentHolderModule::class,
        FeatureManagerModule::class
    ]
)
interface AppComponent : CommonApi {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        @BindsInstance
        fun contextManager(contextManager: ContextManager): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}
