package com.edgeverse.wallet.feature_assets.presentation.send.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.showWallet
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_account_api.view.showAddress
import com.edgeverse.wallet.feature_account_api.view.showChain
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureComponent
import com.edgeverse.wallet.feature_assets.presentation.send.TransferDraft
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendAmount
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendConfirm
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendContainer
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendFee
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendNetwork
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendRecipient
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendSender
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendToolbar
import kotlinx.android.synthetic.main.fragment_confirm_send.confirmSendWallet

private const val KEY_DRAFT = "KEY_DRAFT"

class ConfirmSendFragment : BaseFragment<ConfirmSendViewModel>() {

    companion object {

        fun getBundle(transferDraft: TransferDraft) = Bundle().apply {
            putParcelable(KEY_DRAFT, transferDraft)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_confirm_send, container, false)

    override fun initViews() {
        confirmSendContainer.applyStatusBarInsets()

        confirmSendSender.setOnClickListener { viewModel.senderAddressClicked() }
        confirmSendRecipient.setOnClickListener { viewModel.recipientAddressClicked() }

        confirmSendToolbar.setHomeButtonListener { viewModel.backClicked() }

        confirmSendConfirm.setOnClickListener { viewModel.submitClicked() }
        confirmSendConfirm.prepareForProgress(viewLifecycleOwner)
    }

    override fun inject() {
        val transferDraft = argument<TransferDraft>(KEY_DRAFT)

        FeatureUtils.getFeature<AssetsFeatureComponent>(
            requireContext(),
            AssetsFeatureApi::class.java
        )
            .confirmTransferComponentFactory()
            .create(this, transferDraft)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmSendViewModel) {
        setupExternalActions(viewModel)
        observeValidations(viewModel)
        setupFeeLoading(viewModel, confirmSendFee)

        viewModel.recipientModel.observe(confirmSendRecipient::showAddress)
        viewModel.senderModel.observe(confirmSendSender::showAddress)

        viewModel.sendButtonStateLiveData.observe(confirmSendConfirm::setState)

        viewModel.wallet.observe(confirmSendWallet::showWallet)
        viewModel.chainUi.observe(confirmSendNetwork::showChain)

        viewModel.amountModel.observe(confirmSendAmount::setAmount)
    }
}
