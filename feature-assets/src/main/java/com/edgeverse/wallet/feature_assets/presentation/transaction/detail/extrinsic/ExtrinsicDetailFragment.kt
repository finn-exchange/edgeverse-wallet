package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.extrinsic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.ImageLoader
import coil.load
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.formatDateTime
import com.edgeverse.wallet.common.utils.setTextColorRes
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_account_api.view.showAddress
import com.edgeverse.wallet.feature_account_api.view.showChain
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureComponent
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.feature_assets.presentation.model.showOperationStatus
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailAmount
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailCall
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailHash
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailIcon
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailModule
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailNetwork
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailSender
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailStatus
import kotlinx.android.synthetic.main.fragment_extrinsic_details.extrinsicDetailToolbar
import javax.inject.Inject

private const val KEY_EXTRINSIC = "KEY_EXTRINSIC"

class ExtrinsicDetailFragment : BaseFragment<ExtrinsicDetailViewModel>() {

    companion object {
        fun getBundle(operation: OperationParcelizeModel.Extrinsic) = Bundle().apply {
            putParcelable(KEY_EXTRINSIC, operation)
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_extrinsic_details, container, false)

    override fun initViews() {
        extrinsicDetailToolbar.setHomeButtonListener { viewModel.backClicked() }

        extrinsicDetailHash.setOnClickListener {
            viewModel.extrinsicClicked()
        }

        extrinsicDetailSender.setOnClickListener {
            viewModel.fromAddressClicked()
        }
    }

    override fun inject() {
        val operation = argument<OperationParcelizeModel.Extrinsic>(KEY_EXTRINSIC)

        FeatureUtils.getFeature<AssetsFeatureComponent>(
            requireContext(),
            AssetsFeatureApi::class.java
        )
            .extrinsicDetailComponentFactory()
            .create(this, operation)
            .inject(this)
    }

    override fun subscribe(viewModel: ExtrinsicDetailViewModel) {
        setupExternalActions(viewModel)

        with(viewModel.operation) {
            extrinsicDetailHash.showValue(hash)

            extrinsicDetailStatus.showOperationStatus(statusAppearance)
            extrinsicDetailAmount.setTextColorRes(statusAppearance.amountTint)

            extrinsicDetailToolbar.setTitle(time.formatDateTime(requireContext()))
            extrinsicDetailModule.showValue(module)
            extrinsicDetailCall.showValue(call)

            extrinsicDetailAmount.text = fee
        }

        viewModel.senderAddressModelFlow.observe(extrinsicDetailSender::showAddress)

        viewModel.chainUi.observe {
            extrinsicDetailNetwork.showChain(it)

            extrinsicDetailIcon.load(it.icon, imageLoader)
        }
    }
}
