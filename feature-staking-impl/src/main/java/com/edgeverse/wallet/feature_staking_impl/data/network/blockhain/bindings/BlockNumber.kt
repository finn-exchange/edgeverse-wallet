package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.bindings

import com.edgeverse.wallet.common.data.network.runtime.binding.HelperBinding
import com.edgeverse.wallet.common.data.network.runtime.binding.cast
import com.edgeverse.wallet.feature_staking_api.domain.model.BlockNumber

@HelperBinding
fun bindBlockNumber(dynamicInstance: Any?): BlockNumber {
    return dynamicInstance.cast()
}
