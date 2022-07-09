package com.edgeverse.wallet.feature_crowdloan_impl.data

import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.runtime.ext.isUtilityAsset
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.state.SingleAssetSharedState

private const val CROWDLOAN_SHARED_STATE = "CROWDLOAN_SHARED_STATE"

class CrowdloanSharedState(
    chainRegistry: ChainRegistry,
    preferences: Preferences,
) : SingleAssetSharedState(
    preferences = preferences,
    chainRegistry = chainRegistry,
    filter = { chain, chainAsset -> chain.hasCrowdloans and chainAsset.isUtilityAsset },
    preferencesKey = CROWDLOAN_SHARED_STATE
)
