package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import dev.chrisbanes.insetter.applyInsetter
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeBrowserEvents
import com.dfinn.wallet.common.mixin.impl.observeRetries
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.utils.bindTo
import com.dfinn.wallet.common.utils.setVisible
import com.dfinn.wallet.common.view.setProgress
import com.dfinn.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.dfinn.wallet.feature_crowdloan_impl.R
import com.dfinn.wallet.feature_crowdloan_impl.di.CrowdloanFeatureComponent
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeAmount
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeBonus
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeBonusReward
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeContainer
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeContinue
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeFee
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeLearnMore
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeLeasingPeriod
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeReward
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeToolbar
import kotlinx.android.synthetic.main.fragment_contribute.crowdloanContributeUnlockHint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

private const val KEY_PAYLOAD = "KEY_PAYLOAD"

class CrowdloanContributeFragment : BaseFragment<CrowdloanContributeViewModel>() {

    @Inject protected lateinit var imageLoader: ImageLoader

    companion object {

        const val KEY_BONUS_LIVE_DATA = "KEY_BONUS_LIVE_DATA"

        fun getBundle(payload: ContributePayload) = Bundle().apply {
            putParcelable(KEY_PAYLOAD, payload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_contribute, container, false)
    }

    override fun initViews() {
        crowdloanContributeContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }

            consume(true)
        }

        crowdloanContributeToolbar.setHomeButtonListener { viewModel.backClicked() }
        crowdloanContributeContinue.prepareForProgress(viewLifecycleOwner)
        crowdloanContributeContinue.setOnClickListener { viewModel.nextClicked() }

        crowdloanContributeLearnMore.setOnClickListener { viewModel.learnMoreClicked() }

        crowdloanContributeBonus.setOnClickListener { viewModel.bonusClicked() }
    }

    override fun inject() {
        val payload = argument<ContributePayload>(KEY_PAYLOAD)

        FeatureUtils.getFeature<CrowdloanFeatureComponent>(
            requireContext(),
            CrowdloanFeatureApi::class.java
        )
            .selectContributeFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: CrowdloanContributeViewModel) {
        observeRetries(viewModel)
        observeBrowserEvents(viewModel)
        observeValidations(viewModel)

        viewModel.showNextProgress.observe(crowdloanContributeContinue::setProgress)

        viewModel.assetModelFlow.observe {
            crowdloanContributeAmount.setAssetBalance(it.assetBalance)
            crowdloanContributeAmount.setAssetName(it.tokenName)
            crowdloanContributeAmount.loadAssetImage(it.imageUrl)
        }

        crowdloanContributeAmount.amountInput.bindTo(viewModel.enteredAmountFlow, lifecycleScope)

        viewModel.enteredFiatAmountFlow.observe {
            it.let(crowdloanContributeAmount::setFiatAmount)
        }

        viewModel.feeLiveData.observe(crowdloanContributeFee::setFeeStatus)

        viewModel.estimatedRewardFlow.observe { reward ->
            crowdloanContributeReward.setVisible(reward != null)

            reward?.let {
                crowdloanContributeReward.showValue(reward)
            }
        }

        viewModel.unlockHintFlow.observe(crowdloanContributeUnlockHint::setText)

        viewModel.crowdloanDetailModelFlow.observe {
            crowdloanContributeLeasingPeriod.showValue(it.leasePeriod, it.leasedUntil)
        }

        crowdloanContributeToolbar.setTitle(viewModel.title)

        crowdloanContributeLearnMore.setVisible(viewModel.learnCrowdloanModel != null)

        viewModel.learnCrowdloanModel?.let {
            crowdloanContributeLearnMore.title.text = it.text
            crowdloanContributeLearnMore.loadIcon(it.iconLink, imageLoader)
        }

        viewModel.bonusDisplayFlow.observe {
            crowdloanContributeBonus.setVisible(it != null)

            crowdloanContributeBonusReward.text = it
        }

        viewModel.customizationConfiguration.filterNotNull().observe { (customization, customViewState) ->
            customization.injectViews(crowdloanContributeContainer, customViewState, viewLifecycleOwner.lifecycleScope)
        }
    }
}
