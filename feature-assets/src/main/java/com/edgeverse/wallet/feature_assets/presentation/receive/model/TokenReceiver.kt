package com.edgeverse.wallet.feature_assets.presentation.receive.model

import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.chain.ChainUi

class TokenReceiver(
    val addressModel: AddressModel,
    val chain: ChainUi,
)
