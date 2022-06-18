package com.dfinn.wallet.root.di

import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.mixin.api.NetworkStateMixin
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.systemCall.SystemCallExecutor
import com.dfinn.wallet.core.updater.UpdateSystem
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_assets.data.buyToken.BuyTokenRegistry
import com.dfinn.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.dfinn.wallet.feature_staking_api.domain.api.StakingRepository
import com.dfinn.wallet.feature_wallet_api.di.Wallet
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.connection.ChainConnection
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
