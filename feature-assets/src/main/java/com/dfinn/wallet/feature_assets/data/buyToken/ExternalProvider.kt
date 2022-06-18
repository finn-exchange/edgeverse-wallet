package com.dfinn.wallet.feature_assets.data.buyToken

import android.content.Context

interface ExternalProvider : com.dfinn.wallet.feature_assets.data.buyToken.BuyTokenRegistry.Provider<ExternalProvider.Integrator> {

    companion object {
        const val REDIRECT_URL_BASE = "nova://buy-success"
    }

    interface Integrator : com.dfinn.wallet.feature_assets.data.buyToken.BuyTokenRegistry.Integrator<Context>
}
