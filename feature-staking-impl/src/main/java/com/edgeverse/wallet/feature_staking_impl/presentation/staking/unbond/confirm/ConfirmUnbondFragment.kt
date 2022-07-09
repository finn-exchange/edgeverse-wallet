package com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.hints.observeHints
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.view.setProgress
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import kotlinx.android.synthetic.main.fragment_confirm_unbond.confirmUnbondAmount
import kotlinx.android.synthetic.main.fragment_confirm_unbond.confirmUnbondConfirm
import kotlinx.android.synthetic.main.fragment_confirm_unbond.confirmUnbondExtrinsicInformation
import kotlinx.android.synthetic.main.fragment_confirm_unbond.confirmUnbondHints
import kotlinx.android.synthetic.main.fragment_confirm_unbond.confirmUnbondToolbar

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

class ConfirmUnbondFragment : BaseFragment<ConfirmUnbondViewModel>() {

    companion object {

        fun getBundle(payload: ConfirmUnbondPayload) = Bundle().apply {
            putParcelable(PAYLOAD_KEY, payload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_unbond, container, false)
    }

    override fun initViews() {
        confirmUnbondToolbar.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        confirmUnbondExtrinsicInformation.setOnAccountClickedListener { viewModel.originAccountClicked() }

        confirmUnbondToolbar.setHomeButtonListener { viewModel.backClicked() }
        confirmUnbondConfirm.prepareForProgress(viewLifecycleOwner)
        confirmUnbondConfirm.setOnClickListener { viewModel.confirmClicked() }
    }

    override fun inject() {
        val payload = argument<ConfirmUnbondPayload>(PAYLOAD_KEY)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .confirmUnbondFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmUnbondViewModel) {
        observeValidations(viewModel)
        setupExternalActions(viewModel)
        observeHints(viewModel.hintsMixin, confirmUnbondHints)

        viewModel.amountModelFlow.observe(confirmUnbondAmount::setAmount)

        viewModel.showNextProgress.observe(confirmUnbondConfirm::setProgress)

        viewModel.walletUiFlow.observe(confirmUnbondExtrinsicInformation::setWallet)
        viewModel.feeStatusLiveData.observe(confirmUnbondExtrinsicInformation::setFeeStatus)
        viewModel.originAddressModelFlow.observe(confirmUnbondExtrinsicInformation::setAccount)
    }
}
