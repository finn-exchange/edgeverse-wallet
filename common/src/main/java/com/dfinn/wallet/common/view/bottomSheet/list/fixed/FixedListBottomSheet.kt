package com.dfinn.wallet.common.view.bottomSheet.list.fixed

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.dfinn.wallet.common.R
import com.dfinn.wallet.common.utils.DialogExtensions
import com.dfinn.wallet.common.utils.inflateChild
import com.dfinn.wallet.common.utils.setDrawableStart
import com.dfinn.wallet.common.utils.setVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheeet_fixed_list.fixedListSheetDivider
import kotlinx.android.synthetic.main.bottom_sheeet_fixed_list.fixedListSheetItemContainer
import kotlinx.android.synthetic.main.bottom_sheeet_fixed_list.fixedListSheetTitle
import kotlinx.android.synthetic.main.item_sheet_iconic_label.view.itemExternalActionContent

abstract class FixedListBottomSheet(context: Context) :
    BottomSheetDialog(context, R.style.BottomSheetDialog), DialogExtensions {

    init {
        setContentView(R.layout.bottom_sheeet_fixed_list)
    }

    final override val dialogInterface: DialogInterface
        get() = this

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    final override fun setContentView(layoutResId: Int) {
        super.setContentView(layoutResId)
    }

    override fun setTitle(@StringRes titleRes: Int) {
        fixedListSheetTitle.setText(titleRes)
    }

    override fun setTitle(title: CharSequence?) {
        fixedListSheetTitle.text = title
    }

    fun item(@LayoutRes layoutRes: Int, builder: (View) -> Unit) {
        val view = fixedListSheetItemContainer.inflateChild(layoutRes)

        builder.invoke(view)

        fixedListSheetItemContainer.addView(view)
    }

    fun <T : View> item(view: T, builder: (T) -> Unit) {
        builder.invoke(view)

        fixedListSheetItemContainer.addView(view)
    }

    protected fun setTitleDividerVisible(visible: Boolean) {
        fixedListSheetDivider.setVisible(visible, falseState = View.INVISIBLE)
    }
}

fun FixedListBottomSheet.item(@DrawableRes icon: Int, title: String, onClick: (View) -> Unit) {
    item(R.layout.item_sheet_iconic_label) { view ->
        view.itemExternalActionContent.text = title
        view.itemExternalActionContent.setDrawableStart(
            drawableRes = icon,
            widthInDp = 24,
            tint = R.color.white,
            paddingInDp = 16
        )

        view.setDismissingClickListener(onClick)
    }
}

fun FixedListBottomSheet.item(@DrawableRes icon: Int, @StringRes titleRes: Int, onClick: (View) -> Unit) {
    item(icon, context.getString(titleRes), onClick)
}
