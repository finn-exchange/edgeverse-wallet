package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.clear
import com.edgeverse.wallet.common.list.PayloadGenerator
import com.edgeverse.wallet.common.list.resolvePayload
import com.edgeverse.wallet.common.utils.images.setIcon
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions.model.ContributionModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_contribution.view.itemContributionAmount
import kotlinx.android.synthetic.main.item_contribution.view.itemContributionIcon
import kotlinx.android.synthetic.main.item_contribution.view.itemContributionName

class UserContributionsAdapter(
    private val imageLoader: ImageLoader,
) : ListAdapter<ContributionModel, ContributionHolder>(ContributionCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributionHolder {
        return ContributionHolder(imageLoader, parent.inflateChild(R.layout.item_contribution))
    }

    override fun onBindViewHolder(holder: ContributionHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ContributionHolder, position: Int, payloads: MutableList<Any>) {
        resolvePayload(holder, position, payloads) {
            when (it) {
                ContributionModel::amount -> holder.bindAmount(getItem(position))
            }
        }
    }

    override fun onViewRecycled(holder: ContributionHolder) {
        holder.unbind()
    }
}

private object ContributionPayloadGenerator : PayloadGenerator<ContributionModel>(
    ContributionModel::amount
)

private object ContributionCallback : DiffUtil.ItemCallback<ContributionModel>() {

    override fun areItemsTheSame(oldItem: ContributionModel, newItem: ContributionModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ContributionModel, newItem: ContributionModel): Boolean {
        return true
    }

    override fun getChangePayload(oldItem: ContributionModel, newItem: ContributionModel): Any? {
        return ContributionPayloadGenerator.diff(oldItem, newItem)
    }
}

class ContributionHolder(
    private val imageLoader: ImageLoader,
    override val containerView: View,
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        item: ContributionModel,
    ) = with(containerView) {
        itemContributionIcon.setIcon(item.icon, imageLoader)
        itemContributionName.text = item.title

        bindAmount(item)
    }

    fun unbind() {
        with(containerView) {
            itemContributionIcon.clear()
        }
    }

    fun bindAmount(item: ContributionModel) {
        containerView.itemContributionAmount.text = item.amount
    }
}
