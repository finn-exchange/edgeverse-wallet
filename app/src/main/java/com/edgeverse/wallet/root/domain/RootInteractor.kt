package com.edgeverse.wallet.root.domain

import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.feature_assets.data.buyToken.ExternalProvider
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RootInteractor(
    private val updateSystem: UpdateSystem,
    private val walletRepository: WalletRepository,
) {

    fun runBalancesUpdate(): Flow<Updater.SideEffect> = updateSystem.start()

    fun isBuyProviderRedirectLink(link: String) = ExternalProvider.REDIRECT_URL_BASE in link

    fun stakingAvailableFlow() = flowOf(true) // TODO remove this logic

    suspend fun updatePhishingAddresses() {
        runCatching {
            walletRepository.updatePhishingAddresses()
        }
    }
}
