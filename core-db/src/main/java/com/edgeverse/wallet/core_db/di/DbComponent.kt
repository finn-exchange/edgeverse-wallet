package com.edgeverse.wallet.core_db.di

import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.ApplicationScope
import dagger.Component

@Component(
    modules = [
        DbModule::class
    ],
    dependencies = [
        DbDependencies::class
    ]
)
@ApplicationScope
abstract class DbComponent : DbApi {

    @Component(
        dependencies = [
            CommonApi::class
        ]
    )
    interface DbDependenciesComponent : DbDependencies
}
