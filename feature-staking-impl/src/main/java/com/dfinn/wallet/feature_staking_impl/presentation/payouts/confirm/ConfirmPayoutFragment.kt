package com.dfinn.wallet.feature_staking_impl.presentation.payouts.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeRetries
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.utils.applyStatusBarInsets
import com.dfinn.wallet.common.view.setProgress
import com.dfinn.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.dfinn.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_confirm_payout.confirmPayoutAmount
import kotlinx.android.synthetic.main.fragment_confirm_payout.confirmPayoutConfirm
import kotlinx.android.synthetic.main.fragment_confirm_payout.confirmPayoutContainer
import kotlinx.android.synthetic.main.fragment_confirm_payout.confirmPayoutExtrinsicInformation
import kotlinx.android.synthetic.main.fragment_confirm_payout.confirmPayoutToolbar

class ConfirmPayoutFragment : BaseFragment<ConfirmPayoutViewModel>() {

    companion object {
        private const val KEY_PAYOUTS = "payouts"

        fun getBundle(payload: ConfirmPayoutPayload): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PAYOUTS, payload)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_payout, container, false)
    }

    override fun initViews() {
        confirmPayoutContainer.applyStatusBarInsets()

        confirmPayoutConfirm.setOnClickListener { viewModel.submitClicked() }
        confirmPayoutConfirm.prepareForProgress(viewLifecycleOwner)

        confirmPayoutToolbar.setHomeButtonListener { viewModel.backClicked() }

        confirmPayoutExtrinsicInformation.setOnAccountClickedListener { viewModel.accountClicked() }
    }

    override fun inject() {
        val payload = argument<ConfirmPayoutPayload>(KEY_PAYOUTS)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .confirmPayoutFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmPayoutViewModel) {
        setupExternalActions(viewModel)
        observeValidations(viewModel)
        observeRetries(viewModel)
        setupFeeLoading(viewModel, confirmPayoutExtrinsicInformation.fee)

        viewModel.initiatorAddressModel.observe(confirmPayoutExtrinsicInformation::setAccount)
        viewModel.walletUiFlow.observe(confirmPayoutExtrinsicInformation::setWallet)

        viewModel.totalRewardFlow.observe(confirmPayoutAmount::setAmount)

        viewModel.showNextProgress.observe(confirmPayoutConfirm::setProgress)
    }
}
