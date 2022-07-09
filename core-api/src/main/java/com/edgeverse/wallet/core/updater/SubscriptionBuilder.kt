package com.edgeverse.wallet.core.updater

import com.edgeverse.wallet.core.model.StorageChange
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.Flow

interface SubscriptionBuilder {

    val socketService: SocketService

    fun subscribe(key: String): Flow<StorageChange>
}
