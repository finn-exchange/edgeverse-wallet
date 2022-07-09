package com.edgeverse.wallet.feature_dapp_impl.web3.states.hostApi

import com.edgeverse.wallet.common.address.AddressModel

class AuthorizeDAppPayload(
    val title: String,
    val dAppIconUrl: String?,
    val dAppUrl: String,
    val walletAddressModel: AddressModel
)
