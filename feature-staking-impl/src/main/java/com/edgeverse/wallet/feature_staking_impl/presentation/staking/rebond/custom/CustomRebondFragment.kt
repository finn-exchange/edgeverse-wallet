package com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.hints.observeHints
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.common.view.setProgress
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.amountChooser.setupAmountChooser
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import com.edgeverse.wallet.feature_wallet_api.presentation.view.showAmount
import kotlinx.android.synthetic.main.fragment_rebond_custom.rebondAmount
import kotlinx.android.synthetic.main.fragment_rebond_custom.rebondContinue
import kotlinx.android.synthetic.main.fragment_rebond_custom.rebondFee
import kotlinx.android.synthetic.main.fragment_rebond_custom.rebondHints
import kotlinx.android.synthetic.main.fragment_rebond_custom.rebondToolbar
import kotlinx.android.synthetic.main.fragment_rebond_custom.rebondTransferable

class CustomRebondFragment : BaseFragment<CustomRebondViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_rebond_custom, container, false)
    }

    override fun initViews() {
        rebondToolbar.applyStatusBarInsets()

        rebondToolbar.setHomeButtonListener { viewModel.backClicked() }
        rebondContinue.prepareForProgress(viewLifecycleOwner)
        rebondContinue.setOnClickListener { viewModel.confirmClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .rebondCustomFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: CustomRebondViewModel) {
        observeValidations(viewModel)
        observeHints(viewModel.hintsMixin, rebondHints)
        setupAmountChooser(viewModel.amountChooserMixin, rebondAmount)
        setupFeeLoading(viewModel, rebondFee)

        viewModel.transferableFlow.observe(rebondTransferable::showAmount)

        viewModel.showNextProgress.observe(rebondContinue::setProgress)
    }
}
