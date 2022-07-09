package com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.common.view.setProgress
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_redeem.redeemAmount
import kotlinx.android.synthetic.main.fragment_redeem.redeemConfirm
import kotlinx.android.synthetic.main.fragment_redeem.redeemContainer
import kotlinx.android.synthetic.main.fragment_redeem.redeemExtrinsicInformation
import kotlinx.android.synthetic.main.fragment_redeem.redeemToolbar

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

class RedeemFragment : BaseFragment<RedeemViewModel>() {

    companion object {

        fun getBundle(payload: RedeemPayload) = Bundle().apply {
            putParcelable(PAYLOAD_KEY, payload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_redeem, container, false)
    }

    override fun initViews() {
        redeemContainer.applyStatusBarInsets()

        redeemToolbar.setHomeButtonListener { viewModel.backClicked() }
        redeemConfirm.prepareForProgress(viewLifecycleOwner)
        redeemConfirm.setOnClickListener { viewModel.confirmClicked() }

        redeemExtrinsicInformation.setOnAccountClickedListener { viewModel.originAccountClicked() }
    }

    override fun inject() {
        val payload = argument<RedeemPayload>(PAYLOAD_KEY)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .redeemFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: RedeemViewModel) {
        observeValidations(viewModel)
        setupExternalActions(viewModel)
        setupFeeLoading(viewModel, redeemExtrinsicInformation.fee)

        viewModel.showNextProgress.observe(redeemConfirm::setProgress)

        viewModel.amountModelFlow.observe(redeemAmount::setAmount)

        viewModel.walletUiFlow.observe(redeemExtrinsicInformation::setWallet)
        viewModel.feeLiveData.observe(redeemExtrinsicInformation::setFeeStatus)
        viewModel.originAddressModelFlow.observe(redeemExtrinsicInformation::setAccount)
    }
}
