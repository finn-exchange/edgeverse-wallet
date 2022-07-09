package com.edgeverse.wallet.common.data.holders

interface ChainIdHolder {

    suspend fun chainId(): String
}
