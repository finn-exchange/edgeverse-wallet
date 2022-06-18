package com.dfinn.wallet.feature_staking_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.dfinn.wallet.common.utils.makeGone
import com.dfinn.wallet.common.utils.makeVisible
import com.dfinn.wallet.common.utils.setVisible
import com.dfinn.wallet.common.view.TableView
import com.dfinn.wallet.feature_account_api.view.showAddress
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.presentation.common.rewardDestination.RewardDestinationModel
import kotlinx.android.synthetic.main.view_reward_destination_viewer.view.viewRewardDestinationDestination
import kotlinx.android.synthetic.main.view_reward_destination_viewer.view.viewRewardDestinationPayoutAccount

class RewardDestinationViewer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : TableView(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.view_reward_destination_viewer, this)
    }

    fun showRewardDestination(rewardDestinationModel: RewardDestinationModel?) {
        if (rewardDestinationModel == null) {
            makeGone()
            return
        }

        makeVisible()
        viewRewardDestinationPayoutAccount.setVisible(rewardDestinationModel is RewardDestinationModel.Payout)

        when (rewardDestinationModel) {
            is RewardDestinationModel.Restake -> {
                viewRewardDestinationDestination.showValue(context.getString(R.string.staking_setup_restake_v2_2_0))
            }
            is RewardDestinationModel.Payout -> {
                viewRewardDestinationDestination.showValue(context.getString(R.string.staking_reward_destination_payout))
                viewRewardDestinationPayoutAccount.showAddress(rewardDestinationModel.destination)
            }
        }
    }

    fun setPayoutAccountClickListener(listener: (View) -> Unit) {
        viewRewardDestinationPayoutAccount.setOnClickListener(listener)
    }
}
