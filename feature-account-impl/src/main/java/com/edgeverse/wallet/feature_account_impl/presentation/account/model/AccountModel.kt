package com.edgeverse.wallet.feature_account_impl.presentation.account.model

import android.graphics.drawable.PictureDrawable
import com.edgeverse.wallet.core.model.Network
import com.edgeverse.wallet.feature_account_impl.presentation.view.advanced.encryption.model.CryptoTypeModel

data class AccountModel(
    val address: String,
    val name: String?,
    val image: PictureDrawable,
    val accountIdHex: String,
    val position: Int,
    val cryptoTypeModel: CryptoTypeModel,
    val network: Network
)
