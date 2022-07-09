package com.edgeverse.wallet.feature_assets.presentation.model

import androidx.annotation.ColorRes
import com.edgeverse.wallet.common.utils.images.Icon

class OperationModel(
    val id: String,
    val formattedTime: String,
    val amount: String,
    @ColorRes val amountColorRes: Int,
    val header: String,
    val statusAppearance: OperationStatusAppearance,
    val operationIcon: Icon,
    val subHeader: String
)
