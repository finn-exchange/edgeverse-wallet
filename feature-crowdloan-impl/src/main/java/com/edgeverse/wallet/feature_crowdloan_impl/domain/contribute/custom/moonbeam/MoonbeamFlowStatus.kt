package com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId

sealed class MoonbeamFlowStatus {

    object RegionNotSupported : MoonbeamFlowStatus()

    object Completed : MoonbeamFlowStatus()

    class NeedsChainAccount(val chainId: ChainId, val metaId: Long) : MoonbeamFlowStatus()

    object UnsupportedAccountEncryption : MoonbeamFlowStatus()

    object ReadyToComplete : MoonbeamFlowStatus()
}
