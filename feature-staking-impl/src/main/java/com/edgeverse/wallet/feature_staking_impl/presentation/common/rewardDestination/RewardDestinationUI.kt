package com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination

import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_account_api.presenatation.account.chooser.AccountChooserBottomSheetDialog
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.presentation.view.RewardDestinationChooserView

fun <V> BaseFragment<V>.observeRewardDestinationChooser(
    viewModel: V,
    chooser: RewardDestinationChooserView,
) where V : BaseViewModel, V : RewardDestinationMixin {
    viewModel.rewardDestinationModelFlow.observe {
        chooser.payoutTitle.setVisible(it is RewardDestinationModel.Payout)
        chooser.payoutTarget.setVisible(it is RewardDestinationModel.Payout)
        chooser.destinationRestake.setChecked(it is RewardDestinationModel.Restake)
        chooser.destinationPayout.setChecked(it is RewardDestinationModel.Payout)

        if (it is RewardDestinationModel.Payout) {
            chooser.payoutTarget.setAddressModel(it.destination)
        }
    }

    viewModel.rewardReturnsLiveData.observe {
        chooser.destinationPayout.setPercentageGain(it.payout.gain)
        chooser.destinationPayout.setTokenAmount(it.payout.amount)
        chooser.destinationPayout.setFiatAmount(it.payout.fiatAmount)

        chooser.destinationRestake.setPercentageGain(it.restake.gain)
        chooser.destinationRestake.setTokenAmount(it.restake.amount)
        chooser.destinationRestake.setFiatAmount(it.restake.fiatAmount)
    }

    viewModel.showDestinationChooserEvent.observeEvent {
        AccountChooserBottomSheetDialog(
            context = requireContext(),
            payload = it,
            onSuccess = viewModel::payoutDestinationChanged,
            onCancel = null,
            title = R.string.staking_select_payout_account
        ).show()
    }

    chooser.destinationPayout.setOnClickListener { viewModel.payoutClicked(viewModel) }
    chooser.destinationRestake.setOnClickListener { viewModel.restakeClicked() }
    chooser.payoutTarget.setActionClickListener { viewModel.payoutTargetClicked(viewModel) }
    chooser.learnMore.setOnClickListener { viewModel.learnMoreClicked() }
}
