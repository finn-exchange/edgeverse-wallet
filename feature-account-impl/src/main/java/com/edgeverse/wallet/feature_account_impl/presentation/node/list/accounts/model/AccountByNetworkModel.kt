package com.edgeverse.wallet.feature_account_impl.presentation.node.list.accounts.model

import com.edgeverse.wallet.common.address.AddressModel

data class AccountByNetworkModel(
    val nodeId: Int,
    val accountAddress: String,
    val name: String?,
    val addressModel: AddressModel
)
