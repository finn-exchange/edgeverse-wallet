package com.dfinn.wallet.feature_staking_impl.data

import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.state.SingleAssetSharedState

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
