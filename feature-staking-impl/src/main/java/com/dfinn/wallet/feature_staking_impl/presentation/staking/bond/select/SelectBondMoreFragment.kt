package com.dfinn.wallet.feature_staking_impl.presentation.staking.bond.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.hints.observeHints
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.view.setProgress
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.setupAmountChooser
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_bond_more.bondMoreAmount
import kotlinx.android.synthetic.main.fragment_bond_more.bondMoreContainer
import kotlinx.android.synthetic.main.fragment_bond_more.bondMoreContinue
import kotlinx.android.synthetic.main.fragment_bond_more.bondMoreFee
import kotlinx.android.synthetic.main.fragment_bond_more.bondMoreHints
import kotlinx.android.synthetic.main.fragment_bond_more.bondMoreToolbar

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

class SelectBondMoreFragment : BaseFragment<SelectBondMoreViewModel>() {

    companion object {

        fun getBundle(payload: SelectBondMorePayload) = Bundle().apply {
            putParcelable(PAYLOAD_KEY, payload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bond_more, container, false)
    }

    override fun initViews() {
        bondMoreContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }

            consume(true)
        }

        bondMoreToolbar.setHomeButtonListener { viewModel.backClicked() }
        bondMoreContinue.prepareForProgress(viewLifecycleOwner)
        bondMoreContinue.setOnClickListener { viewModel.nextClicked() }
    }

    override fun inject() {
        val payload = argument<SelectBondMorePayload>(PAYLOAD_KEY)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .selectBondMoreFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: SelectBondMoreViewModel) {
        observeValidations(viewModel)
        setupAmountChooser(viewModel.amountChooserMixin, bondMoreAmount)
        setupFeeLoading(viewModel, bondMoreFee)
        observeHints(viewModel.hintsMixin, bondMoreHints)

        viewModel.showNextProgress.observe(bondMoreContinue::setProgress)

        viewModel.feeLiveData.observe(bondMoreFee::setFeeStatus)
    }
}
