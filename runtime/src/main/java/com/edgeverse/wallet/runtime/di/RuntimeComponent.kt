package com.edgeverse.wallet.runtime.di

import dagger.Component
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import com.edgeverse.wallet.core_db.di.DbApi

@Component(
    modules = [
        RuntimeModule::class,
        ChainRegistryModule::class
    ],
    dependencies = [
        RuntimeDependencies::class
    ]
)
@ApplicationScope
abstract class RuntimeComponent : RuntimeApi {

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
        ]
    )
    interface RuntimeDependenciesComponent : RuntimeDependencies
}
