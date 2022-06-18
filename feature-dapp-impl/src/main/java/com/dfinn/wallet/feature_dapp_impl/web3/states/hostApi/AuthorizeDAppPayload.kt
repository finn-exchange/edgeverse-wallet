package com.dfinn.wallet.feature_dapp_impl.web3.states.hostApi

import com.dfinn.wallet.common.address.AddressModel

class AuthorizeDAppPayload(
    val title: String,
    val dAppIconUrl: String?,
    val dAppUrl: String,
    val walletAddressModel: AddressModel
)
