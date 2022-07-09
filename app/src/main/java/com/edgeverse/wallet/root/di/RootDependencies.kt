package com.edgeverse.wallet.root.di

import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.mixin.api.NetworkStateMixin
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.systemCall.SystemCallExecutor
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_assets.data.buyToken.BuyTokenRegistry
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_wallet_api.di.Wallet
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.flow.MutableStateFlow

interface RootDependencies {

    fun crowdloanRepository(): CrowdloanRepository

    fun networkStateMixin(): NetworkStateMixin

    fun externalRequirementsFlow(): MutableStateFlow<ChainConnection.ExternalRequirement>

    fun accountRepository(): AccountRepository

    fun walletRepository(): WalletRepository

    fun appLinksProvider(): AppLinksProvider

    fun buyTokenRegistry(): BuyTokenRegistry

    fun resourceManager(): ResourceManager

    @Wallet
    fun walletUpdateSystem(): UpdateSystem

    fun stakingRepository(): StakingRepository

    fun chainRegistry(): ChainRegistry

    val systemCallExecutor: SystemCallExecutor
}
