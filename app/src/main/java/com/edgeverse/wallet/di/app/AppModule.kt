package com.edgeverse.wallet.di.app

import android.content.Context
import com.edgeverse.wallet.App
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideContext(application: App): Context {
        return application
    }
}
