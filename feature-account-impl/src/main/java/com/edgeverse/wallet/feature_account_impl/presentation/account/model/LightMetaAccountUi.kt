package com.edgeverse.wallet.feature_account_impl.presentation.account.model

import android.graphics.drawable.Drawable
import com.edgeverse.wallet.common.utils.IgnoredOnEquals

data class LightMetaAccountUi(
    val id: Long,
    val name: String,
    val isSelected: Boolean,
    val picture: IgnoredOnEquals<Drawable>,
)
