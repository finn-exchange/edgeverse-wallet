package com.edgeverse.wallet.feature_account_impl.presentation.pincode

import android.os.Parcelable
import androidx.annotation.StringRes
import com.edgeverse.wallet.common.navigation.DelayedNavigation
import com.edgeverse.wallet.feature_account_impl.R
import kotlinx.android.parcel.Parcelize

@Parcelize
class ToolbarConfiguration(@StringRes val titleRes: Int? = null, val backVisible: Boolean = false) : Parcelable

sealed class PinCodeAction(open val toolbarConfiguration: ToolbarConfiguration) : Parcelable {

    @Parcelize class Create(val delayedNavigation: DelayedNavigation) :
        PinCodeAction(ToolbarConfiguration(R.string.pincode_title_create, false))

    @Parcelize class Check(
        val delayedNavigation: DelayedNavigation,
        override val toolbarConfiguration: ToolbarConfiguration
    ) : PinCodeAction(toolbarConfiguration)

    @Parcelize object Change :
        PinCodeAction(ToolbarConfiguration(R.string.profile_pincode_change_title, true))
}
