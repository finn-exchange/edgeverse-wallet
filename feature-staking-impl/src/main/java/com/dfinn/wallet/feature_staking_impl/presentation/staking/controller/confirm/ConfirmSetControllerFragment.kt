package com.dfinn.wallet.feature_staking_impl.presentation.staking.controller.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.utils.applyStatusBarInsets
import com.dfinn.wallet.common.view.setProgress
import com.dfinn.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.dfinn.wallet.feature_account_api.view.showAddress
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import kotlinx.android.synthetic.main.fragment_confirm_set_controller.confirmSetControllerConfirm
import kotlinx.android.synthetic.main.fragment_confirm_set_controller.confirmSetControllerController
import kotlinx.android.synthetic.main.fragment_confirm_set_controller.confirmSetControllerExtrinsicInformation
import kotlinx.android.synthetic.main.fragment_confirm_set_controller.confirmSetControllerToolbar

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

class ConfirmSetControllerFragment : BaseFragment<ConfirmSetControllerViewModel>() {
    companion object {
        fun getBundle(payload: ConfirmSetControllerPayload) = Bundle().apply {
            putParcelable(PAYLOAD_KEY, payload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_set_controller, container, false)
    }

    override fun initViews() {
        confirmSetControllerToolbar.applyStatusBarInsets()

        confirmSetControllerToolbar.setHomeButtonListener { viewModel.back() }

        confirmSetControllerConfirm.setOnClickListener { viewModel.confirmClicked() }
        confirmSetControllerConfirm.prepareForProgress(viewLifecycleOwner)

        confirmSetControllerExtrinsicInformation.setOnAccountClickedListener { viewModel.stashClicked() }

        confirmSetControllerController.setOnClickListener { viewModel.controllerClicked() }
    }

    override fun inject() {
        val payload = argument<ConfirmSetControllerPayload>(PAYLOAD_KEY)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .confirmSetControllerFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmSetControllerViewModel) {
        observeValidations(viewModel)
        setupExternalActions(viewModel)

        viewModel.walletUiFlow.observe(confirmSetControllerExtrinsicInformation::setWallet)
        viewModel.stashAddressFlow.observe(confirmSetControllerExtrinsicInformation::setAccount)
        viewModel.feeStatusFlow.observe(confirmSetControllerExtrinsicInformation::setFeeStatus)

        viewModel.controllerAddressLiveData.observe(confirmSetControllerController::showAddress)

        viewModel.submittingInProgress.observe(confirmSetControllerConfirm::setProgress)
    }
}
