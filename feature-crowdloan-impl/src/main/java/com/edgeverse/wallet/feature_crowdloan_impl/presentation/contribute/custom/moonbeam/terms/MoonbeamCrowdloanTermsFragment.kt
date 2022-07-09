package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import dev.chrisbanes.insetter.applyInsetter
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeBrowserEvents
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.common.view.ButtonState
import com.edgeverse.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.di.CrowdloanFeatureComponent
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import kotlinx.android.synthetic.main.fragment_moonbeam_terms.moonbeamTermsCheckbox
import kotlinx.android.synthetic.main.fragment_moonbeam_terms.moonbeamTermsConfirm
import kotlinx.android.synthetic.main.fragment_moonbeam_terms.moonbeamTermsContainer
import kotlinx.android.synthetic.main.fragment_moonbeam_terms.moonbeamTermsFee
import kotlinx.android.synthetic.main.fragment_moonbeam_terms.moonbeamTermsLink
import kotlinx.android.synthetic.main.fragment_moonbeam_terms.moonbeamTermsToolbar
import javax.inject.Inject

private const val KEY_PAYLOAD = "KEY_PAYLOAD"

class MoonbeamCrowdloanTermsFragment : BaseFragment<MoonbeamCrowdloanTermsViewModel>() {

    @Inject
    protected lateinit var imageLoader: ImageLoader

    companion object {

        fun getBundle(payload: ContributePayload) = Bundle().apply {
            putParcelable(KEY_PAYLOAD, payload)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_moonbeam_terms, container, false)
    }

    override fun initViews() {
        moonbeamTermsContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }

            consume(true)
        }

        moonbeamTermsToolbar.setHomeButtonListener { viewModel.backClicked() }

        moonbeamTermsConfirm.prepareForProgress(viewLifecycleOwner)
        moonbeamTermsConfirm.setOnClickListener { viewModel.submitClicked() }

        moonbeamTermsLink.setOnClickListener { viewModel.termsLinkClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<CrowdloanFeatureComponent>(
            requireContext(),
            CrowdloanFeatureApi::class.java
        )
            .moonbeamTermsFactory()
            .create(this, argument(KEY_PAYLOAD))
            .inject(this)
    }

    override fun subscribe(viewModel: MoonbeamCrowdloanTermsViewModel) {
        setupFeeLoading(viewModel, moonbeamTermsFee)
        observeBrowserEvents(viewModel)
        observeValidations(viewModel)

        moonbeamTermsLink.title.text = viewModel.termsLinkContent.title
        moonbeamTermsLink.icon.load(viewModel.termsLinkContent.iconUrl, imageLoader)

        moonbeamTermsCheckbox.bindTo(viewModel.termsCheckedFlow, viewLifecycleOwner.lifecycleScope)

        viewModel.submitButtonState.observe {
            when (it) {
                is SubmitActionState.Loading -> moonbeamTermsConfirm.setState(ButtonState.PROGRESS)
                is SubmitActionState.Unavailable -> {
                    moonbeamTermsConfirm.setState(ButtonState.DISABLED)
                    moonbeamTermsConfirm.text = it.reason
                }
                is SubmitActionState.Available -> {
                    moonbeamTermsConfirm.setState(ButtonState.NORMAL)
                    moonbeamTermsConfirm.setText(R.string.common_apply)
                }
            }
        }
    }
}
