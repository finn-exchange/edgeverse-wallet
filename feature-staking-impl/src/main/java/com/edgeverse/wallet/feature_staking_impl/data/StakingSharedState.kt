package com.edgeverse.wallet.feature_staking_impl.data

import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState

private const val STAKING_SHARED_STATE = "STAKING_SHARED_STATE"

class StakingSharedState(
    chainRegistry: ChainRegistry,
    preferences: Preferences,
) : SingleAssetSharedState(
    preferences = preferences,
    chainRegistry = chainRegistry,
    filter = { _, chainAsset -> chainAsset.staking != Chain.Asset.StakingType.UNSUPPORTED },
    preferencesKey = STAKING_SHARED_STATE
)
