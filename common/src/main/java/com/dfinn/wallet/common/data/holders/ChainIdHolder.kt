package com.dfinn.wallet.common.data.holders

interface ChainIdHolder {

    suspend fun chainId(): String
}
