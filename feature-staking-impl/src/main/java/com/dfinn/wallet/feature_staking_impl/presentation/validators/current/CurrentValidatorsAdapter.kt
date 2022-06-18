package com.dfinn.wallet.feature_staking_impl.presentation.validators.current

import android.view.View
import android.view.ViewGroup
import com.dfinn.wallet.common.list.BaseGroupedDiffCallback
import com.dfinn.wallet.common.list.GroupedListAdapter
import com.dfinn.wallet.common.list.GroupedListHolder
import com.dfinn.wallet.common.utils.dp
import com.dfinn.wallet.common.utils.inflateChild
import com.dfinn.wallet.common.utils.setDrawableStart
import com.dfinn.wallet.common.utils.setTextColorRes
import com.dfinn.wallet.common.utils.setTextOrHide
import com.dfinn.wallet.common.utils.setVisible
import com.dfinn.wallet.common.utils.updatePadding
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.presentation.validators.current.model.NominatedValidatorModel
import com.dfinn.wallet.feature_staking_impl.presentation.validators.current.model.NominatedValidatorStatusModel
import kotlinx.android.synthetic.main.item_current_validator.view.currentValidatorSlashedIcon
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorApy
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorIcon
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorInfo
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorName
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorNominated
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorNominatedAmount
import kotlinx.android.synthetic.main.item_current_validator.view.itemCurrentValidatorOversubscribed
import kotlinx.android.synthetic.main.item_current_validator_group.view.itemCurrentValidatorContainer
import kotlinx.android.synthetic.main.item_current_validator_group.view.itemCurrentValidatorGroupDescription
import kotlinx.android.synthetic.main.item_current_validator_group.view.itemCurrentValidatorGroupStatus

class CurrentValidatorsAdapter(
    private val handler: Handler,
) : GroupedListAdapter<NominatedValidatorStatusModel, NominatedValidatorModel>(CurrentValidatorsDiffCallback) {

    interface Handler {

        fun infoClicked(validatorModel: NominatedValidatorModel)
    }

    override fun createGroupViewHolder(parent: ViewGroup): GroupedListHolder {
        return CurrentValidatorsGroupHolder(parent.inflateChild(R.layout.item_current_validator_group))
    }

    override fun createChildViewHolder(parent: ViewGroup): GroupedListHolder {
        return CurrentValidatorsChildHolder(parent.inflateChild(R.layout.item_current_validator))
    }

    override fun bindGroup(holder: GroupedListHolder, group: NominatedValidatorStatusModel) {
        (holder as CurrentValidatorsGroupHolder).bind(group)
    }

    override fun bindChild(holder: GroupedListHolder, child: NominatedValidatorModel) {
        (holder as CurrentValidatorsChildHolder).bind(child, handler)
    }
}

private class CurrentValidatorsGroupHolder(view: View) : GroupedListHolder(view) {

    fun bind(group: NominatedValidatorStatusModel) = with(containerView) {
        val topPadding = if (isFirst()) 16 else 24
        itemCurrentValidatorContainer.updatePadding(top = topPadding.dp(context))

        itemCurrentValidatorGroupStatus.setTextOrHide(group.titleConfig?.text)

        group.titleConfig?.let {
            itemCurrentValidatorGroupStatus.setTextColorRes(it.textColorRes)
            itemCurrentValidatorGroupStatus.setDrawableStart(it.iconRes, widthInDp = 16, paddingInDp = 8, tint = it.iconTintRes)
        }

        itemCurrentValidatorGroupDescription.text = group.description
    }

    private fun isFirst() = absoluteAdapterPosition == 0
}

private class CurrentValidatorsChildHolder(view: View) : GroupedListHolder(view) {

    fun bind(validator: NominatedValidatorModel, handler: CurrentValidatorsAdapter.Handler) = with(containerView) {
        itemCurrentValidatorIcon.setImageDrawable(validator.addressModel.image)
        itemCurrentValidatorName.text = validator.addressModel.nameOrAddress

        itemCurrentValidatorNominated.setVisible(validator.nominated != null)
        itemCurrentValidatorNominatedAmount.text = validator.nominated?.token

        itemCurrentValidatorApy.setTextOrHide(validator.apy)

        itemCurrentValidatorInfo.setOnClickListener { handler.infoClicked(validator) }

        itemCurrentValidatorOversubscribed.setVisible(validator.isOversubscribed)
        currentValidatorSlashedIcon.setVisible(validator.isSlashed)
    }
}

private object CurrentValidatorsDiffCallback :
    BaseGroupedDiffCallback<NominatedValidatorStatusModel, NominatedValidatorModel>(NominatedValidatorStatusModel::class.java) {

    override fun areGroupItemsTheSame(oldItem: NominatedValidatorStatusModel, newItem: NominatedValidatorStatusModel): Boolean {
        return oldItem == newItem
    }

    override fun areGroupContentsTheSame(oldItem: NominatedValidatorStatusModel, newItem: NominatedValidatorStatusModel): Boolean {
        return true
    }

    override fun areChildItemsTheSame(oldItem: NominatedValidatorModel, newItem: NominatedValidatorModel): Boolean {
        return oldItem.addressModel.address == newItem.addressModel.address
    }

    override fun areChildContentsTheSame(oldItem: NominatedValidatorModel, newItem: NominatedValidatorModel): Boolean {
        return oldItem.nominated == newItem.nominated
    }
}
