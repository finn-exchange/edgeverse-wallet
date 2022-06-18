package com.dfinn.wallet.feature_account_impl.di

import dagger.BindsInstance
import dagger.Component
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.core_db.di.DbApi
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.dfinn.wallet.feature_account_impl.presentation.account.advancedEncryption.di.AdvancedEncryptionComponent
import com.dfinn.wallet.feature_account_impl.presentation.account.create.di.CreateAccountComponent
import com.dfinn.wallet.feature_account_impl.presentation.account.details.di.AccountDetailsComponent
import com.dfinn.wallet.feature_account_impl.presentation.account.edit.di.AccountEditComponent
import com.dfinn.wallet.feature_account_impl.presentation.account.list.di.AccountListComponent
import com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm.ShareCompletedReceiver
import com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm.di.ExportJsonConfirmComponent
import com.dfinn.wallet.feature_account_impl.presentation.exporting.json.password.di.ExportJsonPasswordComponent
import com.dfinn.wallet.feature_account_impl.presentation.exporting.seed.di.ExportSeedComponent
import com.dfinn.wallet.feature_account_impl.presentation.importing.di.ImportAccountComponent
import com.dfinn.wallet.feature_account_impl.presentation.language.di.LanguagesComponent
import com.dfinn.wallet.feature_account_impl.presentation.mnemonic.backup.di.BackupMnemonicComponent
import com.dfinn.wallet.feature_account_impl.presentation.mnemonic.confirm.di.ConfirmMnemonicComponent
import com.dfinn.wallet.feature_account_impl.presentation.node.add.di.AddNodeComponent
import com.dfinn.wallet.feature_account_impl.presentation.node.details.di.NodeDetailsComponent
import com.dfinn.wallet.feature_account_impl.presentation.node.list.di.NodesComponent
import com.dfinn.wallet.feature_account_impl.presentation.pincode.di.PinCodeComponent
import com.dfinn.wallet.feature_account_impl.presentation.settings.di.SettingsComponent
import com.dfinn.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        AccountFeatureDependencies::class,
    ],
    modules = [
        AccountFeatureModule::class,
        ExportModule::class
    ]
)
@FeatureScope
interface AccountFeatureComponent : AccountFeatureApi {

    fun createAccountComponentFactory(): CreateAccountComponent.Factory

    fun advancedEncryptionComponentFactory(): AdvancedEncryptionComponent.Factory

    fun importAccountComponentFactory(): ImportAccountComponent.Factory

    fun backupMnemonicComponentFactory(): BackupMnemonicComponent.Factory

    fun profileComponentFactory(): SettingsComponent.Factory

    fun pincodeComponentFactory(): PinCodeComponent.Factory

    fun confirmMnemonicComponentFactory(): ConfirmMnemonicComponent.Factory

    fun accountsComponentFactory(): AccountListComponent.Factory

    fun editAccountsComponentFactory(): AccountEditComponent.Factory

    fun accountDetailsComponentFactory(): AccountDetailsComponent.Factory

    fun connectionsComponentFactory(): NodesComponent.Factory

    fun nodeDetailsComponentFactory(): NodeDetailsComponent.Factory

    fun languagesComponentFactory(): LanguagesComponent.Factory

    fun addNodeComponentFactory(): AddNodeComponent.Factory

    fun exportSeedFactory(): ExportSeedComponent.Factory

    fun exportJsonPasswordFactory(): ExportJsonPasswordComponent.Factory

    fun exportJsonConfirmFactory(): ExportJsonConfirmComponent.Factory

    fun inject(receiver: ShareCompletedReceiver)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance accountRouter: AccountRouter,
            @BindsInstance advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
            deps: AccountFeatureDependencies
        ): AccountFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            RuntimeApi::class,
            DbApi::class
        ]
    )
    interface AccountFeatureDependenciesComponent : AccountFeatureDependencies
}
