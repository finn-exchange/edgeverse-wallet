package com.edgeverse.wallet.feature_wallet_impl.data.mappers

import com.edgeverse.wallet.core_db.model.AssetWithToken
import com.edgeverse.wallet.core_db.model.TokenLocal
import com.edgeverse.wallet.feature_wallet_api.domain.model.Asset
import com.edgeverse.wallet.feature_wallet_api.domain.model.Token
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

fun mapTokenLocalToToken(
    tokenLocal: TokenLocal,
    chainAsset: Chain.Asset,
): Token {
    return with(tokenLocal) {
        Token(
            configuration = chainAsset,
            dollarRate = dollarRate,
            recentRateChange = recentRateChange
        )
    }
}

fun mapAssetLocalToAsset(
    assetLocal: AssetWithToken,
    chainAsset: Chain.Asset
): Asset {
    return with(assetLocal) {
        Asset(
            token = mapTokenLocalToToken(token, chainAsset),
            frozenInPlanks = asset.frozenInPlanks,
            freeInPlanks = asset.freeInPlanks,
            reservedInPlanks = asset.reservedInPlanks,
            bondedInPlanks = asset.bondedInPlanks,
            unbondingInPlanks = asset.unbondingInPlanks,
            redeemableInPlanks = asset.redeemableInPlanks
        )
    }
}
