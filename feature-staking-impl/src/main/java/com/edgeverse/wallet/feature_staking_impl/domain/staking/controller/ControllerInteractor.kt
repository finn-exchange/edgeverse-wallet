package com.edgeverse.wallet.feature_staking_impl.domain.staking.controller

import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.calls.setController
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.ext.multiAddressOf
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

class ControllerInteractor(
    private val extrinsicService: ExtrinsicService,
    private val sharedStakingSate: StakingSharedState
) {
    suspend fun estimateFee(controllerAccountAddress: String): BigInteger {
        return withContext(Dispatchers.IO) {
            val chain = sharedStakingSate.chain()

            extrinsicService.estimateFee(chain) {
                setController(chain.multiAddressOf(controllerAccountAddress))
            }
        }
    }

    suspend fun setController(stashAccountAddress: String, controllerAccountAddress: String): Result<String> {
        return withContext(Dispatchers.IO) {
            val chain = sharedStakingSate.chain()
            val accountId = chain.accountIdOf(stashAccountAddress)

            extrinsicService.submitExtrinsic(chain, accountId) {
                setController(chain.multiAddressOf(controllerAccountAddress))
            }
        }
    }
}
