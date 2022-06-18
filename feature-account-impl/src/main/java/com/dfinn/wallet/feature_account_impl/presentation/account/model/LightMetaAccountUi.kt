package com.dfinn.wallet.feature_account_impl.presentation.account.model

import android.graphics.drawable.Drawable
import com.dfinn.wallet.common.utils.IgnoredOnEquals

data class LightMetaAccountUi(
    val id: Long,
    val name: String,
    val isSelected: Boolean,
    val picture: IgnoredOnEquals<Drawable>,
)
