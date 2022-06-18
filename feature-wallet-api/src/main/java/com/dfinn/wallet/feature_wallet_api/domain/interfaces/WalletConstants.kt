package com.dfinn.wallet.feature_wallet_api.domain.interfaces

import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import java.math.BigInteger

interface WalletConstants {

    suspend fun existentialDeposit(chainId: ChainId): BigInteger
}
