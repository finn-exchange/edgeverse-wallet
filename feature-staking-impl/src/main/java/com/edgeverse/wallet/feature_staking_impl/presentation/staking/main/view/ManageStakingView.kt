package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.edgeverse.wallet.common.utils.WithContextExtensions
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.updatePadding
import com.edgeverse.wallet.common.view.shape.getBlurDrawable
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.manage.ManageStakeAction
import kotlinx.android.synthetic.main.view_manage_staking.view.manageStakingBondMore
import kotlinx.android.synthetic.main.view_manage_staking.view.manageStakingController
import kotlinx.android.synthetic.main.view_manage_staking.view.manageStakingPayouts
import kotlinx.android.synthetic.main.view_manage_staking.view.manageStakingRewardDestination
import kotlinx.android.synthetic.main.view_manage_staking.view.manageStakingUnbond
import kotlinx.android.synthetic.main.view_manage_staking.view.manageStakingValidators

typealias ManageStakeActionClickListener = (ManageStakeAction) -> Unit

class ManageStakingView @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle),
    WithContextExtensions by WithContextExtensions(context) {

    private val actionToViewMapping by lazy(LazyThreadSafetyMode.NONE) {
        mapOf(
            ManageStakeAction.BOND_MORE to manageStakingBondMore,
            ManageStakeAction.UNBOND to manageStakingUnbond,
            ManageStakeAction.REWARD_DESTINATION to manageStakingRewardDestination,
            ManageStakeAction.PAYOUTS to manageStakingPayouts,
            ManageStakeAction.VALIDATORS to manageStakingValidators,
            ManageStakeAction.CONTROLLER to manageStakingController,
        )
    }

    private var listener: ManageStakeActionClickListener? = null

    init {
        View.inflate(context, R.layout.view_manage_staking, this)

        orientation = VERTICAL
        updatePadding(top = 8.dp, bottom = 8.dp)

        background = context.getBlurDrawable()
        clipToOutline = true

        actionToViewMapping.forEach { (action, view) ->
            view.setOnClickListener {
                listener?.invoke(action)
            }
        }
    }

    fun setAvailableActions(actions: Collection<ManageStakeAction>) {
        actionToViewMapping.values.forEach(View::makeGone)

        actions.forEach {
            actionToViewMapping.getValue(it).makeVisible()
        }
    }

    fun onManageStakeActionClicked(listener: ManageStakeActionClickListener) {
        this.listener = listener
    }
}
