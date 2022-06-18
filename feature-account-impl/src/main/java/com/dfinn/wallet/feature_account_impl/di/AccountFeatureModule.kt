package com.dfinn.wallet.feature_account_impl.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.data.network.coingecko.CoingeckoApi
import com.dfinn.wallet.common.data.network.rpc.SocketSingleRequestExecutor
import com.dfinn.wallet.common.data.secrets.v1.SecretStoreV1
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.common.data.storage.encrypt.EncryptedPreferences
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.common.domain.GetAvailableFiatCurrencies
import com.dfinn.wallet.common.domain.SelectedFiat
import com.dfinn.wallet.common.resources.ClipboardManager
import com.dfinn.wallet.common.resources.LanguagesHolder
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.systemCall.SystemCallExecutor
import com.dfinn.wallet.core_db.dao.AccountDao
import com.dfinn.wallet.core_db.dao.MetaAccountDao
import com.dfinn.wallet.core_db.dao.NodeDao
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActionsProvider
import com.dfinn.wallet.feature_account_api.presenatation.mixin.addressInput.AddressInputMixinFactory
import com.dfinn.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.dfinn.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserProvider
import com.dfinn.wallet.feature_account_impl.data.network.blockchain.AccountSubstrateSource
import com.dfinn.wallet.feature_account_impl.data.network.blockchain.AccountSubstrateSourceImpl
import com.dfinn.wallet.feature_account_impl.data.repository.AccountRepositoryImpl
import com.dfinn.wallet.feature_account_impl.data.repository.AddAccountRepository
import com.dfinn.wallet.feature_account_impl.data.repository.datasource.AccountDataSource
import com.dfinn.wallet.feature_account_impl.data.repository.datasource.AccountDataSourceImpl
import com.dfinn.wallet.feature_account_impl.data.repository.datasource.migration.AccountDataMigration
import com.dfinn.wallet.feature_account_impl.data.secrets.AccountSecretsFactory
import com.dfinn.wallet.feature_account_impl.domain.AccountInteractorImpl
import com.dfinn.wallet.feature_account_impl.domain.NodeHostValidator
import com.dfinn.wallet.feature_account_impl.domain.account.add.AddAccountInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.dfinn.wallet.feature_account_impl.domain.account.details.AccountDetailsInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.account.wallet.WalletUiUseCaseImpl
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherMixin
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherProvider
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.dfinn.wallet.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedDecoder
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedEncoder

@Module
class AccountFeatureModule {

    @Provides
    @FeatureScope
    fun provideExtrinsicService(
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        rpcCalls: RpcCalls,
        extrinsicBuilderFactory: ExtrinsicBuilderFactory,
    ): ExtrinsicService = ExtrinsicService(
        rpcCalls,
        accountRepository,
        secretStoreV2,
        extrinsicBuilderFactory
    )

    @Provides
    @FeatureScope
    fun provideJsonDecoder(jsonMapper: Gson) = JsonSeedDecoder(jsonMapper)

    @Provides
    @FeatureScope
    fun provideJsonEncoder(
        jsonMapper: Gson,
    ) = JsonSeedEncoder(jsonMapper)

    @Provides
    @FeatureScope
    fun provideAccountRepository(
        accountDataSource: AccountDataSource,
        accountDao: AccountDao,
        nodeDao: NodeDao,
        jsonSeedEncoder: JsonSeedEncoder,
        accountSubstrateSource: AccountSubstrateSource,
        languagesHolder: LanguagesHolder,
        secretStoreV2: SecretStoreV2,
        multiChainQrSharingFactory: MultiChainQrSharingFactory,
    ): AccountRepository {
        return AccountRepositoryImpl(
            accountDataSource,
            accountDao,
            nodeDao,
            jsonSeedEncoder,
            languagesHolder,
            accountSubstrateSource,
            secretStoreV2,
            multiChainQrSharingFactory
        )
    }

    @Provides
    @FeatureScope
    fun provideAccountInteractor(
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
    ): AccountInteractor {
        return AccountInteractorImpl(chainRegistry, accountRepository)
    }

    @Provides
    @FeatureScope
    fun provideAccountDataSource(
        preferences: Preferences,
        encryptedPreferences: EncryptedPreferences,
        nodeDao: NodeDao,
        secretStoreV1: SecretStoreV1,
        accountDataMigration: AccountDataMigration,
        metaAccountDao: MetaAccountDao,
        chainRegistry: ChainRegistry,
        secretStoreV2: SecretStoreV2,
    ): AccountDataSource {
        return AccountDataSourceImpl(
            preferences,
            encryptedPreferences,
            nodeDao,
            metaAccountDao,
            chainRegistry,
            secretStoreV2,
            secretStoreV1,
            accountDataMigration
        )
    }

    @Provides
    fun provideNodeHostValidator() = NodeHostValidator()

    @Provides
    @FeatureScope
    fun provideAccountSubstrateSource(socketRequestExecutor: SocketSingleRequestExecutor): AccountSubstrateSource {
        return AccountSubstrateSourceImpl(socketRequestExecutor)
    }

    @Provides
    @FeatureScope
    fun provideAccountDataMigration(
        preferences: Preferences,
        encryptedPreferences: EncryptedPreferences,
        accountDao: AccountDao,
    ): AccountDataMigration {
        return AccountDataMigration(preferences, encryptedPreferences, accountDao)
    }

    @Provides
    @FeatureScope
    fun provideExternalAccountActions(
        clipboardManager: ClipboardManager,
        resourceManager: ResourceManager,
    ): ExternalActions.Presentation {
        return ExternalActionsProvider(clipboardManager, resourceManager)
    }

    @Provides
    @FeatureScope
    fun provideAccountUpdateScope(
        accountRepository: AccountRepository,
    ) = AccountUpdateScope(accountRepository)

    @Provides
    @FeatureScope
    fun provideAddressDisplayUseCase(
        accountRepository: AccountRepository,
    ) = AddressDisplayUseCase(accountRepository)

    @Provides
    @FeatureScope
    fun provideAccountUseCase(
        accountRepository: AccountRepository,
    ) = SelectedAccountUseCase(accountRepository)

    @Provides
    @FeatureScope
    fun provideAccountDetailsInteractor(
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        chainRegistry: ChainRegistry,
    ) = AccountDetailsInteractor(
        accountRepository,
        secretStoreV2,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideAccountSecretsFactory(
        jsonSeedDecoder: JsonSeedDecoder
    ) = AccountSecretsFactory(jsonSeedDecoder)

    @Provides
    @FeatureScope
    fun provideAddAccountRepository(
        accountDataSource: AccountDataSource,
        accountSecretsFactory: AccountSecretsFactory,
        jsonSeedDecoder: JsonSeedDecoder,
        chainRegistry: ChainRegistry,
    ) = AddAccountRepository(
        accountDataSource,
        accountSecretsFactory,
        jsonSeedDecoder,
        chainRegistry
    )

    @Provides
    @FeatureScope
    fun provideAddAccountInteractor(
        addAccountRepository: AddAccountRepository,
        accountRepository: AccountRepository,
    ) = AddAccountInteractor(addAccountRepository, accountRepository)

    @Provides
    @FeatureScope
    fun provideInteractor(
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        chainRegistry: ChainRegistry,
    ) = AdvancedEncryptionInteractor(accountRepository, secretStoreV2, chainRegistry)

    @Provides
    fun provideImportTypeChooserMixin(): ImportTypeChooserMixin.Presentation = ImportTypeChooserProvider()

    @Provides
    fun provideAddAccountLauncherMixin(
        importTypeChooserMixin: ImportTypeChooserMixin.Presentation,
        resourceManager: ResourceManager,
        router: AccountRouter,
    ): AddAccountLauncherMixin.Presentation = AddAccountLauncherProvider(
        importTypeChooserMixin = importTypeChooserMixin,
        resourceManager = resourceManager,
        router = router
    )

    @Provides
    @FeatureScope
    fun provideAddressInputMixinFactory(
        chainRegistry: ChainRegistry,
        addressIconGenerator: AddressIconGenerator,
        systemCallExecutor: SystemCallExecutor,
        clipboardManager: ClipboardManager,
        multiChainQrSharingFactory: MultiChainQrSharingFactory,
        resourceManager: ResourceManager,
    ) = AddressInputMixinFactory(
        chainRegistry = chainRegistry,
        addressIconGenerator = addressIconGenerator,
        systemCallExecutor = systemCallExecutor,
        clipboardManager = clipboardManager,
        qrSharingFactory = multiChainQrSharingFactory,
        resourceManager = resourceManager
    )

    @Provides
    @FeatureScope
    fun provideWalletUiUseCase(
        accountRepository: AccountRepository,
        addressIconGenerator: AddressIconGenerator
    ): WalletUiUseCase {
        return WalletUiUseCaseImpl(accountRepository, addressIconGenerator)
    }

    @Provides
    @FeatureScope
    fun provideCoingeckoApi(networkApiCreator: NetworkApiCreator): CoingeckoApi {
        return networkApiCreator.create(CoingeckoApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideAvailableFiatCurrenciesUseCase(coingeckoApi: CoingeckoApi) = GetAvailableFiatCurrencies(coingeckoApi)

    @Provides
    @FeatureScope
    fun provideSelectedFiatUseCase(preferences: Preferences) = SelectedFiat(preferences)

}
