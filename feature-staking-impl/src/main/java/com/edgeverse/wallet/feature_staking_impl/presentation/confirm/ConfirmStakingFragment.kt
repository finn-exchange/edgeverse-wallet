package com.edgeverse.wallet.feature_staking_impl.presentation.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.hints.observeHints
import com.edgeverse.wallet.common.mixin.impl.observeRetries
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.view.setProgress
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.showWallet
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_account_api.view.showAddress
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeAccount
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeAmount
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeConfirm
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeFee
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeHints
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeRewardDestination
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeToolbar
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeValidators
import kotlinx.android.synthetic.main.fragment_confirm_stake.confirmStakeWallet
import kotlinx.android.synthetic.main.fragment_confirm_stake.stakingConfirmationContainer

class ConfirmStakingFragment : BaseFragment<ConfirmStakingViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_stake, container, false)
    }

    override fun initViews() {
        stakingConfirmationContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }

            consume(true)
        }

        confirmStakeToolbar.setHomeButtonListener { viewModel.backClicked() }
        onBackPressed { viewModel.backClicked() }

        confirmStakeAccount.setOnClickListener { viewModel.originAccountClicked() }

        confirmStakeConfirm.prepareForProgress(viewLifecycleOwner)
        confirmStakeConfirm.setOnClickListener { viewModel.confirmClicked() }

        confirmStakeValidators.setOnClickListener { viewModel.nominationsClicked() }

        confirmStakeRewardDestination.setPayoutAccountClickListener { viewModel.payoutAccountClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .confirmStakingComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmStakingViewModel) {
        observeRetries(viewModel)
        observeValidations(viewModel)
        setupExternalActions(viewModel)
        setupFeeLoading(viewModel, confirmStakeFee)
        observeHints(viewModel.hintsMixin, confirmStakeHints)

        viewModel.title.observe(confirmStakeToolbar::setTitle)
        viewModel.showNextProgress.observe(confirmStakeConfirm::setProgress)

        viewModel.rewardDestinationFlow.observe {
            confirmStakeRewardDestination.showRewardDestination(it)
        }

        viewModel.amountModel.observe { amountModel ->
            if (amountModel != null) {
                confirmStakeAmount.setAmount(amountModel)
                confirmStakeAmount.makeVisible()
            } else {
                confirmStakeAmount.makeGone()
            }
        }

        viewModel.currentAccountModelFlow.observe(confirmStakeAccount::showAddress)
        viewModel.walletFlow.observe(confirmStakeWallet::showWallet)

        viewModel.nominationsFlow.observe {
            confirmStakeValidators.showValue(it)
        }

        viewModel.amountModel.observe { bondedAmount ->
            confirmStakeAmount.setVisible(bondedAmount != null)
            bondedAmount?.let(confirmStakeAmount::setAmount)
        }
    }
}
