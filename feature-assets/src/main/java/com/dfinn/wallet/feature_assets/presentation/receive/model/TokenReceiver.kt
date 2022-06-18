package com.dfinn.wallet.feature_assets.presentation.receive.model

import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.feature_account_api.presenatation.chain.ChainUi

class TokenReceiver(
    val addressModel: AddressModel,
    val chain: ChainUi,
)
