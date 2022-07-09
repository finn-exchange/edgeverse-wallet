package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.referral

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import coil.ImageLoader
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.common.utils.createSpannable
import com.edgeverse.wallet.common.utils.observe
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.utils.showBrowser
import com.edgeverse.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.di.CrowdloanFeatureComponent
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeView
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeViewState
import kotlinx.android.synthetic.main.view_referral_flow.view.referralBonus
import kotlinx.android.synthetic.main.view_referral_flow.view.referralLearnMore
import kotlinx.android.synthetic.main.view_referral_flow.view.referralNovaBonusApply
import kotlinx.android.synthetic.main.view_referral_flow.view.referralNovaBonusTitle
import kotlinx.android.synthetic.main.view_referral_flow.view.referralPrivacySwitch
import kotlinx.android.synthetic.main.view_referral_flow.view.referralPrivacyText
import kotlinx.android.synthetic.main.view_referral_flow.view.referralReferralCodeInput
import javax.inject.Inject

class ReferralContributeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CustomContributeView(context, attrs, defStyle) {

    @Inject lateinit var imageLoader: ImageLoader

    init {
        View.inflate(context, R.layout.view_referral_flow, this)

        FeatureUtils.getFeature<CrowdloanFeatureComponent>(
            context,
            CrowdloanFeatureApi::class.java
        ).inject(this)

        referralPrivacyText.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun bind(
        viewState: CustomContributeViewState,
        scope: LifecycleCoroutineScope
    ) {
        require(viewState is ReferralContributeViewState)

        referralReferralCodeInput.content.bindTo(viewState.enteredReferralCodeFlow, scope)
        referralPrivacySwitch.bindTo(viewState.privacyAcceptedFlow, scope)

        referralNovaBonusTitle.text = viewState.applyNovaTitle

        viewState.applyNovaCodeEnabledFlow.observe(scope) { enabled ->
            referralNovaBonusApply.isEnabled = enabled

            val applyBonusButtonText = if (enabled) R.string.common_apply else R.string.common_applied
            referralNovaBonusApply.setText(applyBonusButtonText)
        }

        viewState.bonusFlow.observe(scope) { bonus ->
            referralBonus.setVisible(bonus != null)

            bonus?.let { referralBonus.showValue(bonus) }
        }

        with(viewState.learnBonusesTitle) {
            referralLearnMore.loadIcon(iconLink, imageLoader)
            referralLearnMore.title.text = text

            referralLearnMore.setOnClickListener { viewState.learnMoreClicked() }
        }

        referralNovaBonusApply.setOnClickListener { viewState.applyNovaCode() }

        referralPrivacyText.text = createSpannable(context.getString(R.string.onboarding_terms_and_conditions_1_v2_2_0)) {
            clickable(context.getString(R.string.onboarding_terms_and_conditions_2)) {
                viewState.termsClicked()
            }
        }

        viewState.openBrowserFlow.observe(scope) {
            context.showBrowser(it)
        }
    }
}
