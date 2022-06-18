package com.dfinn.wallet.di.app

import com.dfinn.wallet.App
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.modules.CommonModule
import com.dfinn.wallet.common.di.modules.NetworkModule
import com.dfinn.wallet.common.di.scope.ApplicationScope
import com.dfinn.wallet.common.resources.ContextManager
import com.dfinn.wallet.di.app.navigation.NavigationModule
import com.dfinn.wallet.di.deps.ComponentHolderModule
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
