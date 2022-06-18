package com.dfinn.wallet.feature_wallet_impl.data.repository

import com.dfinn.wallet.common.utils.balances
import com.dfinn.wallet.common.utils.numberConstant
import com.dfinn.wallet.feature_wallet_api.domain.interfaces.WalletConstants
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import com.dfinn.wallet.runtime.multiNetwork.getRuntime
import java.math.BigInteger

class RuntimeWalletConstants(
    private val chainRegistry: ChainRegistry
) : WalletConstants {

    override suspend fun existentialDeposit(chainId: ChainId): BigInteger {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.balances().numberConstant("ExistentialDeposit", runtime)
    }
}
