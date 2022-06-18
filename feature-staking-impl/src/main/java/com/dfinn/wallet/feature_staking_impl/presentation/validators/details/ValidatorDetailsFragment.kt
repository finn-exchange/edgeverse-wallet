package com.dfinn.wallet.feature_staking_impl.presentation.validators.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import androidx.core.view.children
import androidx.core.view.updateMarginsRelative
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.utils.addAfter
import com.dfinn.wallet.common.utils.makeGone
import com.dfinn.wallet.common.utils.makeVisible
import com.dfinn.wallet.common.utils.sendEmailIntent
import com.dfinn.wallet.common.view.AlertView
import com.dfinn.wallet.common.view.showValueOrHide
import com.dfinn.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.dfinn.wallet.feature_staking_api.di.StakingFeatureApi
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.dfinn.wallet.feature_staking_impl.presentation.validators.details.model.ValidatorAlert
import com.dfinn.wallet.feature_staking_impl.presentation.validators.parcel.ValidatorDetailsParcelModel
import com.dfinn.wallet.feature_wallet_api.presentation.view.showAmount
import kotlinx.android.synthetic.main.fragment_validator_details.*

class ValidatorDetailsFragment : BaseFragment<ValidatorDetailsViewModel>() {

    companion object {
        private const val KEY_VALIDATOR = "validator"

        fun getBundle(validator: ValidatorDetailsParcelModel): Bundle {
            return Bundle().apply {
                putParcelable(KEY_VALIDATOR, validator)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_validator_details, container, false)
    }

    private val activeStakingFields by lazy(LazyThreadSafetyMode.NONE) {
        listOf(validatorStakingNominators, validatorStakingTotalStake, validatorStakingEstimatedReward)
    }

    override fun initViews() {
        validatorDetailsToolbar.setHomeButtonListener { viewModel.backClicked() }

        validatorStakingTotalStake.setOnClickListener { viewModel.totalStakeClicked() }

        validatorIdentityEmail.setOnClickListener { viewModel.emailClicked() }

        validatorIdentityWeb.setOnClickListener { viewModel.webClicked() }

        validatorAccountInfo.setOnClickListener { viewModel.accountActionsClicked() }
    }

    override fun inject() {
        val validator = argument<ValidatorDetailsParcelModel>(KEY_VALIDATOR)

        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .validatorDetailsComponentFactory()
            .create(this, validator)
            .inject(this)
    }

    override fun subscribe(viewModel: ValidatorDetailsViewModel) {
        setupExternalActions(viewModel)

        viewModel.validatorDetails.observe { validator ->
            with(validator.stake) {
                validatorStakingStatus.showValue(status.text)
                validatorStakingStatus.setPrimaryValueIcon(status.icon, tint = status.iconTint)

                if (activeStakeModel != null) {
                    activeStakingFields.forEach(View::makeVisible)

                    validatorStakingNominators.showValue(activeStakeModel.nominatorsCount, activeStakeModel.maxNominations)
                    validatorStakingTotalStake.showAmount(activeStakeModel.totalStake)
                    validatorStakingEstimatedReward.showValue(activeStakeModel.apy)
                } else {
                    activeStakingFields.forEach(View::makeGone)
                }
            }

            if (validator.identity == null) {
                validatorIdentity.makeGone()
            } else {
                validatorIdentity.makeVisible()

                with(validator.identity) {
                    validatorIdentityLegalName.showValueOrHide(legal)
                    validatorIdentityEmail.showValueOrHide(email)
                    validatorIdentityTwitter.showValueOrHide(twitter)
                    validatorIdentityElementName.showValueOrHide(riot)
                    validatorIdentityWeb.showValueOrHide(web)
                }
            }

            validatorAccountInfo.setAddressModel(validator.addressModel)
        }

        viewModel.errorFlow.observe { alerts ->
            removeAllAlerts()

            val alertViews = alerts.map(::createAlertView)
            validatorDetailsContainer.addAfter(validatorAccountInfo, alertViews)
        }

        viewModel.openEmailEvent.observeEvent {
            requireContext().sendEmailIntent(it)
        }

        viewModel.totalStakeEvent.observeEvent {
            ValidatorStakeBottomSheet(requireContext(), it).show()
        }
    }

    private fun removeAllAlerts() {
        validatorDetailsContainer.children
            .filterIsInstance<AlertView>()
            .forEach(validatorDetailsContainer::removeView)
    }

    private fun createAlertView(alert: ValidatorAlert): AlertView {
        val style = when (alert.severity) {
            ValidatorAlert.Severity.WARNING -> AlertView.Style.WARNING
            ValidatorAlert.Severity.ERROR -> AlertView.Style.ERROR
        }

        return AlertView(requireContext()).also { alertView ->
            alertView.setStyle(style)
            alertView.setText(alert.descriptionRes)

            alertView.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).also { params ->
                params.updateMarginsRelative(start = 16.dp, end = 16.dp, top = 12.dp)
            }
        }
    }
}
