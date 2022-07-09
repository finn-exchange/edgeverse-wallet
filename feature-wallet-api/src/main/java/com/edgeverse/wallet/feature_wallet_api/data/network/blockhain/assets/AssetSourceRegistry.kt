package com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

interface AssetSourceRegistry {

    fun sourceFor(chainAsset: Chain.Asset): AssetSource
}
