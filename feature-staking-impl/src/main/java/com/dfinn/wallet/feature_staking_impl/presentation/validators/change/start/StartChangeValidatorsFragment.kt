package com.dfinn.wallet.feature_staking_impl.presentation.validators.change.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeBrowserEvents
import com.dfinn.wallet.common.view.setProgress
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import kotlinx.android.synthetic.main.fragment_start_change_validators.startChangeValidatorsContainer
import kotlinx.android.synthetic.main.fragment_start_change_validators.startChangeValidatorsCustom
import kotlinx.android.synthetic.main.fragment_start_change_validators.startChangeValidatorsRecommended
import kotlinx.android.synthetic.main.fragment_start_change_validators.startChangeValidatorsToolbar

class StartChangeValidatorsFragment : BaseFragment<StartChangeValidatorsViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start_change_validators, container, false)
    }

    override fun initViews() {
        startChangeValidatorsContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        startChangeValidatorsToolbar.setHomeButtonListener { viewModel.backClicked() }
        onBackPressed { viewModel.backClicked() }

        startChangeValidatorsRecommended.setupAction(viewLifecycleOwner) { viewModel.goToRecommendedClicked() }
        startChangeValidatorsRecommended.setOnLearnMoreClickedListener { viewModel.recommendedLearnMoreClicked() }

        startChangeValidatorsCustom.background = getRoundedCornerDrawable(R.color.white_8).withRipple()
        startChangeValidatorsCustom.setOnClickListener { viewModel.goToCustomClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .startChangeValidatorsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: StartChangeValidatorsViewModel) {
        observeBrowserEvents(viewModel)

        viewModel.validatorsLoading.observe { loading ->
            startChangeValidatorsRecommended.action.setProgress(loading)
            startChangeValidatorsCustom.setInProgress(loading)
        }

        viewModel.customValidatorsTexts.observe {
            startChangeValidatorsToolbar.setTitle(it.toolbarTitle)
            startChangeValidatorsCustom.title.text = it.selectManuallyTitle
            startChangeValidatorsCustom.setBadgeText(it.selectManuallyBadge)
        }
    }
}
