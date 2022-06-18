package com.dfinn.wallet.feature_assets.presentation.send.amount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.utils.applyStatusBarInsets
import com.dfinn.wallet.feature_account_api.presenatation.mixin.addressInput.setupAddressInput
import com.dfinn.wallet.feature_assets.R
import com.dfinn.wallet.feature_assets.di.AssetsFeatureApi
import com.dfinn.wallet.feature_assets.di.AssetsFeatureComponent
import com.dfinn.wallet.feature_assets.presentation.AssetPayload
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.setupAmountChooser
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_select_send.chooseAmountContainer
import kotlinx.android.synthetic.main.fragment_select_send.selectSendAmount
import kotlinx.android.synthetic.main.fragment_select_send.selectSendChain
import kotlinx.android.synthetic.main.fragment_select_send.selectSendFee
import kotlinx.android.synthetic.main.fragment_select_send.selectSendNext
import kotlinx.android.synthetic.main.fragment_select_send.selectSendRecipient
import kotlinx.android.synthetic.main.fragment_select_send.selectSendTitle
import kotlinx.android.synthetic.main.fragment_select_send.selectSendToolbar

private const val KEY_ADDRESS = "KEY_ADDRESS"
private const val KEY_ASSET_PAYLOAD = "KEY_ASSET_PAYLOAD"

class SelectSendFragment : BaseFragment<SelectSendViewModel>() {

    companion object {

        fun getBundle(assetPayload: AssetPayload, recipientAddress: String? = null) = bundleOf(
            KEY_ADDRESS to recipientAddress,
            KEY_ASSET_PAYLOAD to assetPayload
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_select_send, container, false)

    override fun initViews() {
        chooseAmountContainer.applyStatusBarInsets()

        selectSendNext.prepareForProgress(viewLifecycleOwner)
        selectSendNext.setOnClickListener { viewModel.nextClicked() }

        selectSendToolbar.setHomeButtonListener { viewModel.backClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(
            requireContext(),
            AssetsFeatureApi::class.java
        )
            .chooseAmountComponentFactory()
            .create(this, argument(KEY_ADDRESS), argument(KEY_ASSET_PAYLOAD))
            .inject(this)
    }

    override fun subscribe(viewModel: SelectSendViewModel) {
        observeValidations(viewModel)
        setupFeeLoading(viewModel, selectSendFee)
        setupAmountChooser(viewModel.amountChooserMixin, selectSendAmount)
        setupAddressInput(viewModel.addressInputMixin, selectSendRecipient)

        viewModel.chainUi.observe(selectSendChain::setChain)
        viewModel.continueButtonStateLiveData.observe(selectSendNext::setState)
        viewModel.title.observe(selectSendTitle::setText)
    }
}
