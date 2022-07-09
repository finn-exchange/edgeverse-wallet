package com.edgeverse.wallet.feature_wallet_impl.data.repository

import com.edgeverse.wallet.common.utils.balances
import com.edgeverse.wallet.common.utils.numberConstant
import com.edgeverse.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import java.math.BigInteger

class RuntimeWalletConstants(
    private val chainRegistry: ChainRegistry
) : WalletConstants {

    override suspend fun existentialDeposit(chainId: ChainId): BigInteger {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.balances().numberConstant("ExistentialDeposit", runtime)
    }
}
