package com.edgeverse.wallet.feature_account_api.domain.model

import com.edgeverse.wallet.core.model.CryptoType
import com.edgeverse.wallet.core.model.Network
import jp.co.soramitsu.fearless_utils.extensions.fromHex

data class Account(
    val address: String,
    val name: String?,
    val accountIdHex: String,
    val cryptoType: CryptoType, // TODO make optional
    val position: Int,
    val network: Network, // TODO remove when account management will be rewritten,
) {

    val accountId = accountIdHex.fromHex()
}
