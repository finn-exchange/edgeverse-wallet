package com.dfinn.wallet.di.app

import android.content.Context
import com.dfinn.wallet.App
import com.dfinn.wallet.common.di.scope.ApplicationScope
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
