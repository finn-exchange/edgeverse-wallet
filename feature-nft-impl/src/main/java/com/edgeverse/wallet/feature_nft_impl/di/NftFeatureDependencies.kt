package com.edgeverse.wallet.feature_nft_impl.di

import coil.ImageLoader
import com.google.gson.Gson
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.data.network.HttpExceptionHandler
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core_db.dao.NftDao
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.TokenRepository
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.FeeLoaderMixin
import com.edgeverse.wallet.runtime.di.REMOTE_STORAGE_SOURCE
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import javax.inject.Named

interface NftFeatureDependencies {

    fun accountRepository(): AccountRepository

    fun resourceManager(): ResourceManager

    fun selectedAccountUseCase(): SelectedAccountUseCase

    fun addressIconGenerator(): AddressIconGenerator

    fun gson(): Gson

    fun chainRegistry(): ChainRegistry

    fun imageLoader(): ImageLoader

    fun externalAccountActions(): ExternalActions.Presentation

    fun addressDisplayUseCase(): AddressDisplayUseCase

    fun feeLoaderMixinFactory(): FeeLoaderMixin.Factory

    fun extrinsicService(): ExtrinsicService

    fun tokenRepository(): TokenRepository

    fun apiCreator(): NetworkApiCreator

    fun nftDao(): NftDao

    @Named(REMOTE_STORAGE_SOURCE)
    fun remoteStorageSource(): StorageDataSource

    fun exceptionHandler(): HttpExceptionHandler
}
