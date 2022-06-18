package com.dfinn.wallet.feature_assets.di

import com.dfinn.wallet.feature_assets.data.buyToken.BuyTokenRegistry

interface AssetsFeatureApi {

    fun provideBuyTokenRegistry(): BuyTokenRegistry
}
