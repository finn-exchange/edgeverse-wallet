package com.edgeverse.wallet.feature_assets.presentation.receive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.ImageLoader
import coil.load
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.view.shape.getRoundedCornerDrawable
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureComponent
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.receive.model.QrSharingPayload
import kotlinx.android.synthetic.main.fragment_receive.receiveFrom
import kotlinx.android.synthetic.main.fragment_receive.receiveQrCode
import kotlinx.android.synthetic.main.fragment_receive.receiveShare
import kotlinx.android.synthetic.main.fragment_receive.receiveToolbar
import javax.inject.Inject

private const val KEY_PAYLOAD = "KEY_PAYLOAD"

class ReceiveFragment : BaseFragment<ReceiveViewModel>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    companion object {

        fun getBundle(assetPayload: AssetPayload) = Bundle().apply {
            putParcelable(KEY_PAYLOAD, assetPayload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_receive, container, false)

    override fun initViews() {
        receiveFrom.setWholeClickListener { viewModel.recipientClicked() }

        receiveToolbar.setHomeButtonListener { viewModel.backClicked() }

        receiveShare.setOnClickListener { viewModel.shareButtonClicked() }

        receiveFrom.primaryIcon.setVisible(true)

        receiveQrCode.background = requireContext().getRoundedCornerDrawable(fillColorRes = R.color.white)
        receiveQrCode.clipToOutline = true // for round corners
    }

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(
            requireContext(),
            AssetsFeatureApi::class.java
        )
            .receiveComponentFactory()
            .create(this, argument(KEY_PAYLOAD))
            .inject(this)
    }

    override fun subscribe(viewModel: ReceiveViewModel) {
        setupExternalActions(viewModel)

        viewModel.qrBitmapFlow.observe(receiveQrCode::setImageBitmap)

        viewModel.receiver.observe {
            receiveFrom.setTextIcon(it.addressModel.image)
            receiveFrom.primaryIcon.load(it.chain.icon, imageLoader)
            receiveFrom.setMessage(it.addressModel.address)
            receiveFrom.setLabel(it.chain.name)
        }

        viewModel.toolbarTitle.observe(receiveToolbar::setTitle)

        viewModel.shareEvent.observeEvent(::startQrSharingIntent)
    }

    private fun startQrSharingIntent(qrSharingPayload: QrSharingPayload) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, qrSharingPayload.fileUri)
            putExtra(Intent.EXTRA_TEXT, qrSharingPayload.shareMessage)
        }

        startActivity(Intent.createChooser(intent, getString(R.string.wallet_receive_description_v2_2_0)))
    }
}
