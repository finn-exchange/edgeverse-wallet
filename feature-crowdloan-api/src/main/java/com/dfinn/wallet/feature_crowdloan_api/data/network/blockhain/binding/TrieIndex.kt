package com.dfinn.wallet.feature_crowdloan_api.data.network.blockhain.binding

import com.dfinn.wallet.common.data.network.runtime.binding.HelperBinding
import com.dfinn.wallet.common.data.network.runtime.binding.bindNumber
import java.math.BigInteger

typealias TrieIndex = BigInteger

@HelperBinding
fun bindTrieIndex(dynamicInstance: Any?) = bindNumber(dynamicInstance)
