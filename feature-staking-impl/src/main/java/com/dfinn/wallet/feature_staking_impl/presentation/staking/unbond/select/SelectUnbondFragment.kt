package com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.hints.observeHints
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.utils.applyStatusBarInsets
import com.dfinn.wallet.common.view.setProgress
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.setupAmountChooser
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import com.dfinn.wallet.feature_wallet_api.presentation.view.showAmount
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondAmount
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondContainer
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondContinue
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondFee
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondHints
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondToolbar
import kotlinx.android.synthetic.main.fragment_select_unbond.unbondTransferable

class SelectUnbondFragment : BaseFragment<SelectUnbondViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_unbond, container, false)
    }

    override fun initViews() {
        unbondContainer.applyStatusBarInsets()

        unbondToolbar.setHomeButtonListener { viewModel.backClicked() }
        unbondContinue.prepareForProgress(viewLifecycleOwner)
        unbondContinue.setOnClickListener { viewModel.nextClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .selectUnbondFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: SelectUnbondViewModel) {
        observeValidations(viewModel)
        setupFeeLoading(viewModel, unbondFee)
        observeHints(viewModel.hintsMixin, unbondHints)
        setupAmountChooser(viewModel.amountMixin, unbondAmount)

        viewModel.transferableFlow.observe(unbondTransferable::showAmount)

        viewModel.showNextProgress.observe(unbondContinue::setProgress)
    }
}
