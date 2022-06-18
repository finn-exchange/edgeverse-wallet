package com.dfinn.wallet.root.presentation.di

import androidx.appcompat.app.AppCompatActivity
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.root.presentation.RootActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        RootActivityModule::class
    ]
)
@ScreenScope
interface RootActivityComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance activity: AppCompatActivity
        ): RootActivityComponent
    }

    fun inject(rootActivity: RootActivity)
}
