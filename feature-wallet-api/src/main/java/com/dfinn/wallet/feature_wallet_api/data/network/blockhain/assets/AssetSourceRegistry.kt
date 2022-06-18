package com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets

import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain

interface AssetSourceRegistry {

    fun sourceFor(chainAsset: Chain.Asset): AssetSource
}
