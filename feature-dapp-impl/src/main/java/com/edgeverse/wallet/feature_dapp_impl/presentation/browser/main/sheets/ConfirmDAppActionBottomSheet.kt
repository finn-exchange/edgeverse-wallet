package com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main.sheets

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.edgeverse.wallet.common.utils.DialogExtensions
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.presentation.browser.main.DappPendingConfirmation
import kotlinx.android.synthetic.main.bottom_sheet_confirm_dapp_action.confirmDAppActionAllow
import kotlinx.android.synthetic.main.bottom_sheet_confirm_dapp_action.confirmDAppActionReject
import kotlinx.android.synthetic.main.bottom_sheet_confirm_dapp_action.confirmInnerContent

abstract class ConfirmDAppActionBottomSheet<A : DappPendingConfirmation.Action>(
    context: Context,
    protected val confirmation: DappPendingConfirmation<A>,
) : BottomSheetDialog(context, R.style.BottomSheetDialog), DialogExtensions {

    final override val dialogInterface: DialogInterface
        get() = this

    @get:LayoutRes
    abstract val contentLayoutRes: Int

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.bottom_sheet_confirm_dapp_action)
        super.onCreate(savedInstanceState)

        setCancelable(false)

        confirmInnerContent.inflateChild(contentLayoutRes, attachToRoot = true)

        confirmDAppActionAllow.setDismissingClickListener { confirmation.onConfirm() }
        confirmDAppActionReject.setDismissingClickListener { confirmation.onDeny() }
    }
}
