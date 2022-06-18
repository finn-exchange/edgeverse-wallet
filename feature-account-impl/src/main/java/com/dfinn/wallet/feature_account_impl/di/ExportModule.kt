package com.dfinn.wallet.feature_account_impl.di

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_impl.domain.account.export.json.ExportJsonInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.export.seed.ExportSeedInteractor
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry

@Module
class ExportModule {

    @Provides
    @FeatureScope
    fun provideExportJsonInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
    ) = ExportJsonInteractor(
        accountRepository,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideExportMnemonicInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        secretStoreV2: SecretStoreV2,
    ) = ExportMnemonicInteractor(
        accountRepository,
        secretStoreV2,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideExportSeedInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        secretStoreV2: SecretStoreV2,
    ) = ExportSeedInteractor(
        accountRepository,
        secretStoreV2,
        chainRegistry
    )
}
