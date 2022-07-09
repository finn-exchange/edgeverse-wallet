package com.edgeverse.wallet.feature_assets.presentation.receive.model

import android.net.Uri

data class QrSharingPayload(
    val fileUri: Uri,
    val shareMessage: String
)
