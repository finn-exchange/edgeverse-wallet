package com.dfinn.wallet.feature_account_api.data.mappers

import android.graphics.Color
import com.dfinn.wallet.common.utils.floorMod
import com.dfinn.wallet.common.utils.percentageToFraction
import com.dfinn.wallet.feature_account_api.presenatation.chain.ChainUi
import com.dfinn.wallet.feature_account_api.presenatation.chain.GradientUi
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain

fun mapChainToUi(chain: Chain): ChainUi = with(chain) {
    ChainUi(
        id = id,
        name = name,
        gradient = mapGradientToUi(chain.color ?: Chain.Gradient.Default),
        icon = icon
    )
}

fun mapGradientToUi(gradient: Chain.Gradient) = GradientUi(
    angle = cssAngleToAndroid(gradient.angle.toInt()),
    colors = gradient.colors.map(Color::parseColor).toIntArray(),
    positions = gradient.positionsPercent.map(Float::percentageToFraction).toFloatArray()
)

/**
 * Given the following coordinate system
 *          N
 *          |
 *          |
 *          |
 * W--------  --------- E
 *          |
 *          |
 *          |
 *          S
 * Css starts from N clockwise
 * Android starts from E counter-clockwise
 */
fun cssAngleToAndroid(angle: Int) = (-angle + 90) floorMod 360
