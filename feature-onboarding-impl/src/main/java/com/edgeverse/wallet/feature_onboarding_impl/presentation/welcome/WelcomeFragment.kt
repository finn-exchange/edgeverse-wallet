package com.edgeverse.wallet.feature_onboarding_impl.presentation.welcome

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeBrowserEvents
import com.edgeverse.wallet.common.utils.createSpannable
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.setupImportTypeChooser
import com.edgeverse.wallet.feature_onboarding_api.di.OnboardingFeatureApi
import com.edgeverse.wallet.feature_onboarding_impl.R
import com.edgeverse.wallet.feature_onboarding_impl.di.OnboardingFeatureComponent
import kotlinx.android.synthetic.main.fragment_welcome.back
import kotlinx.android.synthetic.main.fragment_welcome.createAccountBtn
import kotlinx.android.synthetic.main.fragment_welcome.importAccountBtn
import kotlinx.android.synthetic.main.fragment_welcome.termsTv

class WelcomeFragment : BaseFragment<WelcomeViewModel>() {

    companion object {
        private const val KEY_DISPLAY_BACK = "display_back"
        private const val KEY_ADD_ACCOUNT_PAYLOAD = "add_account_payload"

        fun bundle(displayBack: Boolean): Bundle {

            return Bundle().apply {
                putBoolean(KEY_DISPLAY_BACK, displayBack)
                putParcelable(KEY_ADD_ACCOUNT_PAYLOAD, AddAccountPayload.MetaAccount)
            }
        }

        fun bundle(payload: AddAccountPayload): Bundle {
            return Bundle().apply {
                putBoolean(KEY_DISPLAY_BACK, true)
                putParcelable(KEY_ADD_ACCOUNT_PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun initViews() {
        configureTermsAndPrivacy(
            getString(R.string.onboarding_terms_and_conditions_1_v2_2_0),
            getString(R.string.onboarding_terms_and_conditions_2),
            getString(R.string.onboarding_privacy_policy)
        )
        termsTv.movementMethod = LinkMovementMethod.getInstance()
        termsTv.highlightColor = Color.TRANSPARENT

        createAccountBtn.setOnClickListener { viewModel.createAccountClicked() }

        importAccountBtn.setOnClickListener { viewModel.importAccountClicked() }

        back.setOnClickListener { viewModel.backClicked() }
    }

    private fun configureTermsAndPrivacy(sourceText: String, terms: String, privacy: String) {
        termsTv.text = createSpannable(sourceText) {
            clickable(terms) {
                viewModel.termsClicked()
            }

            clickable(privacy) {
                viewModel.privacyClicked()
            }
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<OnboardingFeatureComponent>(context!!, OnboardingFeatureApi::class.java)
            .welcomeComponentFactory()
            .create(
                fragment = this,
                shouldShowBack = argument(KEY_DISPLAY_BACK),
                addAccountPayload = argument(KEY_ADD_ACCOUNT_PAYLOAD)
            )
            .inject(this)
    }

    override fun subscribe(viewModel: WelcomeViewModel) {
        viewModel.shouldShowBackLiveData.observe(back::setVisible)

        observeBrowserEvents(viewModel)
        setupImportTypeChooser(viewModel)
    }
}
