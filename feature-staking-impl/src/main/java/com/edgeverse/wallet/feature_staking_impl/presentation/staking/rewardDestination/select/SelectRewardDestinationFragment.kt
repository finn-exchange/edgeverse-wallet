package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeBrowserEvents
import com.edgeverse.wallet.common.mixin.impl.observeRetries
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.view.ButtonState
import com.edgeverse.wallet.common.view.setProgress
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.common.rewardDestination.observeRewardDestinationChooser
import kotlinx.android.synthetic.main.fragment_select_reward_destination.selectRewardDestinationChooser
import kotlinx.android.synthetic.main.fragment_select_reward_destination.selectRewardDestinationContainer
import kotlinx.android.synthetic.main.fragment_select_reward_destination.selectRewardDestinationContinue
import kotlinx.android.synthetic.main.fragment_select_reward_destination.selectRewardDestinationFee
import kotlinx.android.synthetic.main.fragment_select_reward_destination.selectRewardDestinationToolbar

class SelectRewardDestinationFragment : BaseFragment<SelectRewardDestinationViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_select_reward_destination, container, false)
    }

    override fun initViews() {
        selectRewardDestinationContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }

            consume(true)
        }

        selectRewardDestinationToolbar.setHomeButtonListener { viewModel.backClicked() }

        selectRewardDestinationContinue.prepareForProgress(viewLifecycleOwner)
        selectRewardDestinationContinue.setOnClickListener { viewModel.nextClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .selectRewardDestinationFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: SelectRewardDestinationViewModel) {
        observeRetries(viewModel)
        observeValidations(viewModel)
        observeBrowserEvents(viewModel)
        observeRewardDestinationChooser(viewModel, selectRewardDestinationChooser)

        viewModel.showNextProgress.observe(selectRewardDestinationContinue::setProgress)

        viewModel.feeLiveData.observe(selectRewardDestinationFee::setFeeStatus)

        viewModel.continueAvailable.observe {
            val state = if (it) ButtonState.NORMAL else ButtonState.DISABLED

            selectRewardDestinationContinue.setState(state)
        }
    }
}
