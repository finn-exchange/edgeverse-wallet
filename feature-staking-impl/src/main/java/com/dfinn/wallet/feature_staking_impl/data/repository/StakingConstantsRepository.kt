package com.dfinn.wallet.feature_staking_impl.data.repository

import com.dfinn.wallet.common.utils.asNumber
import com.dfinn.wallet.common.utils.constantOrNull
import com.dfinn.wallet.common.utils.numberConstant
import com.dfinn.wallet.common.utils.staking
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import com.dfinn.wallet.runtime.multiNetwork.getRuntime
import java.math.BigInteger

private const val MAX_NOMINATIONS_FALLBACK = 16

class StakingConstantsRepository(
    private val chainRegistry: ChainRegistry,
) {

    suspend fun maxRewardedNominatorPerValidator(chainId: ChainId): Int = getNumberConstant(chainId, "MaxNominatorRewardedPerValidator").toInt()

    suspend fun lockupPeriodInEras(chainId: ChainId): BigInteger = getNumberConstant(chainId, "BondingDuration")

    suspend fun maxValidatorsPerNominator(chainId: ChainId): Int {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.staking().constantOrNull("MaxNominations")?.asNumber(runtime)?.toInt()
            ?: MAX_NOMINATIONS_FALLBACK
    }

    private suspend fun getNumberConstant(chainId: ChainId, constantName: String): BigInteger {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.staking().numberConstant(constantName, runtime)
    }
}
