package com.dfinn.wallet.feature_staking_impl.presentation.payouts.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.utils.setTextColorRes
import com.dfinn.wallet.common.view.startTimer
import com.dfinn.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.dfinn.wallet.feature_account_api.view.showAddress
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.dfinn.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import kotlinx.android.synthetic.main.fragment_payout_details.payoutDetailsAmount
import kotlinx.android.synthetic.main.fragment_payout_details.payoutDetailsContainer
import kotlinx.android.synthetic.main.fragment_payout_details.payoutDetailsEra
import kotlinx.android.synthetic.main.fragment_payout_details.payoutDetailsSubmit
import kotlinx.android.synthetic.main.fragment_payout_details.payoutDetailsToolbar
import kotlinx.android.synthetic.main.fragment_payout_details.payoutDetailsValidator

class PayoutDetailsFragment : BaseFragment<PayoutDetailsViewModel>() {

    companion object {
        private const val KEY_PAYOUT = "payout"

        fun getBundle(payout: PendingPayoutParcelable): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PAYOUT, payout)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_payout_details, container, false)
    }

    override fun initViews() {
        payoutDetailsContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        payoutDetailsToolbar.setHomeButtonListener { viewModel.backClicked() }

        payoutDetailsSubmit.setOnClickListener { viewModel.payoutClicked() }

        payoutDetailsValidator.setOnClickListener { viewModel.validatorExternalActionClicked() }
    }

    override fun inject() {
        val payout = argument<PendingPayoutParcelable>(KEY_PAYOUT)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .payoutDetailsFactory()
            .create(this, payout)
            .inject(this)
    }

    override fun subscribe(viewModel: PayoutDetailsViewModel) {
        setupExternalActions(viewModel)

        viewModel.payoutDetails.observe {
            payoutDetailsToolbar.titleView.startTimer(millis = it.timeLeft, millisCalculatedAt = it.timeLeftCalculatedAt)
            payoutDetailsToolbar.titleView.setTextColorRes(it.timerColor)

            payoutDetailsEra.showValue(it.eraDisplay)
            payoutDetailsValidator.showAddress(it.validatorAddressModel)

            payoutDetailsAmount.setAmount(it.reward)
        }
    }
}
