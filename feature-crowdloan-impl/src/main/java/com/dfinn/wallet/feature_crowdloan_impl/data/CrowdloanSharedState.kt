package com.dfinn.wallet.feature_crowdloan_impl.data

import com.dfinn.wallet.common.data.storage.Preferences
import com.dfinn.wallet.runtime.ext.isUtilityAsset
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.state.SingleAssetSharedState

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
