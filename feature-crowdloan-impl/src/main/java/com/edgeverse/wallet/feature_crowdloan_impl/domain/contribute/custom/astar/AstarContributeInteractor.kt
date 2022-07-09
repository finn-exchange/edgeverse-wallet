package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.astar

import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.blockhain.extrinsic.addMemo
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.ext.isValidAddress
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState
import com.edgeverse.wallet.runtime.state.chain
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder

class AstarContributeInteractor(
    private val selectedAssetSharedState: SingleAssetSharedState,
) {

    suspend fun isReferralCodeValid(code: String): Boolean {
        val currentChain = selectedAssetSharedState.chain()

        return currentChain.isValidAddress(code)
    }

    suspend fun submitOnChain(
        paraId: ParaId,
        referralCode: String,
        extrinsicBuilder: ExtrinsicBuilder,
    ) {
        val currentChain = selectedAssetSharedState.chain()
        val referralAccountId = currentChain.accountIdOf(referralCode)

        extrinsicBuilder.addMemo(paraId, referralAccountId)
    }
}
