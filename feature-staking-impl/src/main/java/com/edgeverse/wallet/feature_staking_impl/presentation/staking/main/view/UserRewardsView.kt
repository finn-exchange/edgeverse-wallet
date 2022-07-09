package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_wallet_api.presentation.model.AmountModel
import kotlinx.android.synthetic.main.view_user_rewards.view.userRewardsContentGroup
import kotlinx.android.synthetic.main.view_user_rewards.view.userRewardsFiatAmount
import kotlinx.android.synthetic.main.view_user_rewards.view.userRewardsFiatAmountShimmer
import kotlinx.android.synthetic.main.view_user_rewards.view.userRewardsShimmerGroup
import kotlinx.android.synthetic.main.view_user_rewards.view.userRewardsTokenAmount
import kotlinx.android.synthetic.main.view_user_rewards.view.userRewardsTokenAmountShimmer

class UserRewardsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.view_user_rewards, this)
    }

    fun showLoading() {
        userRewardsShimmerGroup.makeVisible()
        userRewardsContentGroup.makeGone()

        userRewardsTokenAmountShimmer.startShimmer()
        userRewardsFiatAmountShimmer.startShimmer()
    }

    fun showValue(amountModel: AmountModel) {
        userRewardsShimmerGroup.makeGone()
        userRewardsContentGroup.makeVisible()

        userRewardsTokenAmountShimmer.stopShimmer()
        userRewardsFiatAmountShimmer.stopShimmer()

        userRewardsTokenAmount.text = amountModel.token
        userRewardsFiatAmount.text = amountModel.fiat
    }
}
