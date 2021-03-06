package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edgeverse.wallet.common.utils.inflateChild
import com.edgeverse.wallet.common.utils.removeCompoundDrawables
import com.edgeverse.wallet.common.utils.setDrawableStart
import com.edgeverse.wallet.common.utils.setTextColorRes
import com.edgeverse.wallet.common.view.startTimer
import com.edgeverse.wallet.common.view.stopTimer
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.model.Unbonding
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_unbonding.view.itemUnbondAmount
import kotlinx.android.synthetic.main.item_unbonding.view.itemUnbondStatus
import kotlin.time.ExperimentalTime

class UnbondingsAdapter : ListAdapter<UnbondingModel, UnbondingsHolder>(UnbondingModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnbondingsHolder {
        val view = parent.inflateChild(R.layout.item_unbonding)

        return UnbondingsHolder(view)
    }

    @ExperimentalTime
    override fun onBindViewHolder(holder: UnbondingsHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }
}

class UnbondingsHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    @ExperimentalTime
    fun bind(unbonding: UnbondingModel) = with(containerView) {
        with(unbonding) {
            when (status) {
                Unbonding.Status.Redeemable -> {
                    itemUnbondStatus.setTextColorRes(R.color.green)
                    itemUnbondStatus.removeCompoundDrawables()
                    itemUnbondStatus.stopTimer()
                    itemUnbondStatus.setText(R.string.wallet_balance_redeemable)
                }
                is Unbonding.Status.Unbonding -> {
                    itemUnbondStatus.setTextColorRes(R.color.white_64)
                    itemUnbondStatus.setDrawableStart(R.drawable.ic_time_16, paddingInDp = 4, tint = R.color.white_48)

                    itemUnbondStatus.startTimer(status.timeLeft, status.calculatedAt)
                }
            }

            itemUnbondAmount.text = unbonding.amountModel.token
        }
    }
}

private class UnbondingModelDiffCallback : DiffUtil.ItemCallback<UnbondingModel>() {

    override fun areItemsTheSame(oldItem: UnbondingModel, newItem: UnbondingModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UnbondingModel, newItem: UnbondingModel): Boolean {
        return true
    }
}
