package com.edgeverse.wallet.feature_wallet_api.domain.interfaces

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import java.math.BigInteger

interface WalletConstants {

    suspend fun existentialDeposit(chainId: ChainId): BigInteger
}
