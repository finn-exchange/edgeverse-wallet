package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.dfinn.wallet.common.data.network.runtime.binding.HelperBinding
import com.dfinn.wallet.common.data.network.runtime.binding.cast
import com.dfinn.wallet.feature_staking_api.domain.model.BlockNumber

@HelperBinding
fun bindBlockNumber(dynamicInstance: Any?): BlockNumber {
    return dynamicInstance.cast()
}
