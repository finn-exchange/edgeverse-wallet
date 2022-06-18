package com.dfinn.wallet.feature_assets.presentation.balance.list.view

import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import coil.load
import com.dfinn.wallet.common.list.*
import com.dfinn.wallet.common.utils.format
import com.dfinn.wallet.common.utils.formatAsCurrency
import com.dfinn.wallet.common.utils.inflateChild
import com.dfinn.wallet.common.utils.setTextColorRes
import com.dfinn.wallet.common.view.shape.getCutCornersStateDrawable
import com.dfinn.wallet.feature_assets.R
import com.dfinn.wallet.feature_assets.presentation.balance.list.model.AssetGroupUi
import com.dfinn.wallet.feature_assets.presentation.model.AssetModel
import kotlinx.android.synthetic.main.item_asset.view.*
import kotlinx.android.synthetic.main.item_asset_group.view.*
import java.math.BigDecimal

val dollarRateExtractor = { assetModel: AssetModel -> assetModel.token.dollarRate }
val recentChangeExtractor = { assetModel: AssetModel -> assetModel.token.recentRateChange }

class BalanceListAdapter(
    private val imageLoader: ImageLoader,
    private val itemHandler: ItemAssetHandler,
) : GroupedListAdapter<AssetGroupUi, AssetModel>(DiffCallback) {

    interface ItemAssetHandler {
        fun assetClicked(asset: AssetModel)
    }

    override fun createGroupViewHolder(parent: ViewGroup): GroupedListHolder {
        val view = parent.inflateChild(R.layout.item_asset_group)

        return AssetGroupViewHolder(view)
    }

    override fun createChildViewHolder(parent: ViewGroup): GroupedListHolder {
        val view = parent.inflateChild(R.layout.item_asset)

        return AssetViewHolder(view, imageLoader)
    }

    override fun bindGroup(holder: GroupedListHolder, group: AssetGroupUi) {
        require(holder is AssetGroupViewHolder)

        holder.bind(group)
    }

    override fun bindChild(holder: GroupedListHolder, position: Int, child: AssetModel, payloads: List<Any>) {
        require(holder is AssetViewHolder)

        resolvePayload(holder, position, payloads) {
            when (it) {
                dollarRateExtractor -> holder.bindDollarInfo(child)
                recentChangeExtractor -> holder.bindRecentChange(child)
                AssetModel::total -> holder.bindTotal(child)
            }
        }
    }

    override fun bindChild(holder: GroupedListHolder, child: AssetModel) {
        require(holder is AssetViewHolder)

        holder.bind(child, itemHandler)
    }
}

class AssetGroupViewHolder(
    containerView: View,
) : GroupedListHolder(containerView) {

    fun bind(assetGroup: AssetGroupUi) = with(containerView) {
        itemAssetGroupChain.setChain(assetGroup.chainUi)
        itemAssetGroupBalance.text = assetGroup.groupBalanceFiat
    }
}

class AssetViewHolder(
    containerView: View,
    private val imageLoader: ImageLoader,
) : GroupedListHolder(containerView) {

    fun bind(asset: AssetModel, itemHandler: BalanceListAdapter.ItemAssetHandler) = with(containerView) {
        background = context.getCutCornersStateDrawable()

        itemAssetImage.load(asset.token.configuration.iconUrl, imageLoader)

        bindDollarInfo(asset)

        bindRecentChange(asset)

        bindTotal(asset)

        itemAssetToken.text = asset.token.configuration.symbol

        setOnClickListener { itemHandler.assetClicked(asset) }

    }

    fun bindTotal(asset: AssetModel) {
        containerView.itemAssetBalance.text = asset.total.format()

        bindDollarAmount(asset.dollarAmount)
    }

    fun bindRecentChange(asset: AssetModel) = with(containerView) {
        itemAssetRateChange.setTextColorRes(asset.token.rateChangeColorRes)
        itemAssetRateChange.text = asset.token.recentRateChange
    }

    fun bindDollarInfo(asset: AssetModel) = with(containerView) {
        itemAssetRate.text = asset.token.dollarRate
        bindDollarAmount(asset.dollarAmount)
    }

    private fun bindDollarAmount(dollarAmount: BigDecimal?) {
        containerView.itemAssetDollarAmount.text = dollarAmount?.formatAsCurrency()
    }
}

private object DiffCallback : BaseGroupedDiffCallback<AssetGroupUi, AssetModel>(AssetGroupUi::class.java) {

    override fun areGroupItemsTheSame(oldItem: AssetGroupUi, newItem: AssetGroupUi): Boolean {
        return oldItem.chainUi.id == newItem.chainUi.id
    }

    override fun areGroupContentsTheSame(oldItem: AssetGroupUi, newItem: AssetGroupUi): Boolean {
        return oldItem == newItem
    }

    override fun areChildItemsTheSame(oldItem: AssetModel, newItem: AssetModel): Boolean {
        return oldItem.token.configuration == newItem.token.configuration
    }

    override fun areChildContentsTheSame(oldItem: AssetModel, newItem: AssetModel): Boolean {
        return oldItem == newItem
    }

    override fun getChildChangePayload(oldItem: AssetModel, newItem: AssetModel): Any? {
        return AssetPayloadGenerator.diff(oldItem, newItem)
    }
}

private object AssetPayloadGenerator : PayloadGenerator<AssetModel>(
    dollarRateExtractor, recentChangeExtractor, AssetModel::total
)
