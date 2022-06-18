package com.dfinn.wallet.common.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader
import coil.load
import com.dfinn.wallet.common.R
import com.dfinn.wallet.common.data.network.coingecko.FiatCurrency
import com.dfinn.wallet.common.utils.inflateChild
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.ClickHandler
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListSheetAdapter
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.HolderCreator
import kotlinx.android.synthetic.main.item_fiat_chooser.view.currencyChecked
import kotlinx.android.synthetic.main.item_fiat_chooser.view.currencyIcon
import kotlinx.android.synthetic.main.item_fiat_chooser.view.currencyTitle

class FiatCurrenciesChooserBottomSheetDialog(
    context: Context,
    private val imageLoader: ImageLoader,
    payload: Payload<FiatCurrency>,
    clickHandler: ClickHandler<FiatCurrency>
) :
    DynamicListBottomSheet<FiatCurrency>(
        context, payload, FiatCurrencyDiffCallback, clickHandler
    ) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.common_currency)
    }

    override fun holderCreator(): HolderCreator<FiatCurrency> = { parent ->
        FiatCurrencyHolder(parent.inflateChild(R.layout.item_fiat_chooser), imageLoader)
    }
}

private class FiatCurrencyHolder(parent: View, private val imageLoader: ImageLoader) : DynamicListSheetAdapter.Holder<FiatCurrency>(parent) {

    @OptIn(ExperimentalStdlibApi::class)
    override fun bind(
        item: FiatCurrency,
        isSelected: Boolean,
        handler: DynamicListSheetAdapter.Handler<FiatCurrency>
    ) {
        super.bind(item, isSelected, handler)

        with(itemView) {
            currencyIcon.load(item.icon, imageLoader) {
                this.size(resources.getDimension(R.dimen.x3).toInt())
            }
            currencyTitle.text = item.id.uppercase()
            currencyChecked.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
        }
    }
}

private object FiatCurrencyDiffCallback : DiffUtil.ItemCallback<FiatCurrency>() {
    override fun areItemsTheSame(oldItem: FiatCurrency, newItem: FiatCurrency): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FiatCurrency, newItem: FiatCurrency): Boolean {
        return true
    }
}
