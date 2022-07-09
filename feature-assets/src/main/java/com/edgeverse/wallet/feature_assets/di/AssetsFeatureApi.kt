package com.edgeverse.wallet.feature_assets.di

import com.edgeverse.wallet.feature_assets.data.buyToken.BuyTokenRegistry

interface AssetsFeatureApi {

    fun provideBuyTokenRegistry(): BuyTokenRegistry
}
