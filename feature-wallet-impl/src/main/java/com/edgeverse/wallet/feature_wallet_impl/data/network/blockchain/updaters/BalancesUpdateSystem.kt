package com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.updaters

import android.util.Log
import com.edgeverse.wallet.common.data.network.StorageSubscriptionBuilder
import com.edgeverse.wallet.common.utils.LOG_TAG
import com.edgeverse.wallet.core.updater.UpdateSystem
import com.edgeverse.wallet.core.updater.Updater
import com.edgeverse.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.updaters.balance.PaymentUpdaterFactory
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.getSocket
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.subscribeUsing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class BalancesUpdateSystem(
    private val chainRegistry: ChainRegistry,
    private val paymentUpdaterFactory: PaymentUpdaterFactory,
    private val accountUpdateScope: AccountUpdateScope,
) : UpdateSystem {

    override fun start(): Flow<Updater.SideEffect> {
        return accountUpdateScope.invalidationFlow().flatMapLatest {
            val chains = chainRegistry.currentChains.first()

            val mergedFlow = chains.map { chain ->
                flow {
                    val updater = paymentUpdaterFactory.create(chain)
                    val socket = chainRegistry.getSocket(chain.id)

                    val subscriptionBuilder = StorageSubscriptionBuilder.create(socket)

                    kotlin.runCatching {
                        updater.listenForUpdates(subscriptionBuilder)
                            .catch { logError(chain, it) }
                    }.onSuccess { updaterFlow ->
                        val cancellable = socket.subscribeUsing(subscriptionBuilder.build())

                        updaterFlow.onCompletion { cancellable.cancel() }

                        emitAll(updaterFlow)
                    }.onFailure {
                        logError(chain, it)
                    }
                }
            }.merge()

            mergedFlow
        }.flowOn(Dispatchers.Default)
    }

    private fun logError(chain: Chain, error: Throwable) {
        Log.e(LOG_TAG, "Failed to subscribe to balances in ${chain.name}: ${error.message}", error)
    }
}
