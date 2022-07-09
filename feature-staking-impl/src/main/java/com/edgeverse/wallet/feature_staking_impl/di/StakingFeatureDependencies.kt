package com.edgeverse.wallet.feature_staking_impl.di

import coil.ImageLoader
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.memory.ComputationalCache
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.di.modules.Caching
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.mixin.hints.ResourcesHintsMixinFactory
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.validation.ValidationExecutor
import com.edgeverse.wallet.core.storage.StorageCache
import com.edgeverse.wallet.core_db.dao.AccountStakingDao
import com.edgeverse.wallet.core_db.dao.StakingTotalRewardDao
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_wallet_api.data.cache.AssetCache
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.runtime.di.LOCAL_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.network.rpc.RpcCalls
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import com.google.gson.Gson
import javax.inject.Named

interface StakingFeatureDependencies {

    fun computationalCache(): ComputationalCache

    fun accountRepository(): AccountRepository

    fun storageCache(): StorageCache

    fun bulkRetriever(): BulkRetriever

    fun addressIconGenerator(): AddressIconGenerator

    fun appLinksProvider(): AppLinksProvider

    fun walletRepository(): WalletRepository

    fun tokenRepository(): TokenRepository

    fun resourceManager(): ResourceManager

    fun extrinsicBuilderFactory(): ExtrinsicBuilderFactory

    fun substrateCalls(): RpcCalls

    fun externalAccountActions(): ExternalActions.Presentation

    fun assetCache(): AssetCache

    fun accountStakingDao(): AccountStakingDao

    fun accountUpdateScope(): AccountUpdateScope

    fun stakingTotalRewardsDao(): StakingTotalRewardDao

    fun networkApiCreator(): NetworkApiCreator

    fun httpExceptionHandler(): HttpExceptionHandler

    fun walletConstants(): WalletConstants

    fun gson(): Gson

    fun addressxDisplayUseCase(): AddressDisplayUseCase

    fun extrinsicService(): ExtrinsicService

    fun validationExecutor(): ValidationExecutor

    @Named(REMOTE_STORAGE_SOURCE)
    fun remoteStorageSource(): StorageDataSource

    @Named(LOCAL_STORAGE_SOURCE)
    fun localStorageSource(): StorageDataSource

    fun chainRegistry(): ChainRegistry

    fun imageLoader(): ImageLoader

    fun preferences(): Preferences

    fun feeLoaderMixinFactory(): FeeLoaderMixin.Factory

    val amountChooserMixinFactory: AmountChooserMixin.Factory

    val actionAwaitableMixinFactory: ActionAwaitableMixin.Factory

    @Caching
    fun cachingIconGenerator(): AddressIconGenerator

    val walletUiUseCase: WalletUiUseCase

    val resourcesHintsMixinFactory: ResourcesHintsMixinFactory
}
