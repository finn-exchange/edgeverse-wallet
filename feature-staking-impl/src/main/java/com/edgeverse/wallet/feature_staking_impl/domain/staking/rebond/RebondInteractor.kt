package com.edgeverse.wallet.feature_staking_impl.domain.staking.rebond

import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingState
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.calls.rebond
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

class RebondInteractor(
    private val extrinsicService: ExtrinsicService,
    private val sharedStakingSate: StakingSharedState
) {

    suspend fun estimateFee(amount: BigInteger): BigInteger {
        return withContext(Dispatchers.IO) {
            val chain = sharedStakingSate.chain()

            extrinsicService.estimateFee(chain) {
                rebond(amount)
            }
        }
    }

    suspend fun rebond(stashState: StakingState.Stash, amount: BigInteger): Result<String> {
        return withContext(Dispatchers.IO) {
            extrinsicService.submitExtrinsic(stashState.chain, stashState.controllerId) {
                rebond(amount)
            }
        }
    }
}
