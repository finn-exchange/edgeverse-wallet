package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.lifecycleScope
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.common.view.ButtonState
import com.edgeverse.wallet.common.view.bindFromMap
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationFilter
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationPostProcessor
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.filters.HasIdentityFilter
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.filters.NotOverSubscribedFilter
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.filters.NotSlashedFilter
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.settings.postprocessors.RemoveClusteringPostprocessor
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsApply
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsFilterClustering
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsFilterIdentity
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsFilterOverSubscribed
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsFilterSlashes
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsSort
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsSortOwnStake
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsSortTotalStake
import kotlinx.android.synthetic.main.fragment_custom_validators_settings.customValidatorSettingsToolbar

class CustomValidatorsSettingsFragment : BaseFragment<CustomValidatorsSettingsViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_validators_settings, container, false)
    }

    override fun initViews() {
        customValidatorSettingsApply.setOnClickListener { viewModel.applyChanges() }

        customValidatorSettingsToolbar.setHomeButtonListener { viewModel.backClicked() }
        customValidatorSettingsToolbar.setRightActionClickListener { viewModel.reset() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .customValidatorsSettingsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: CustomValidatorsSettingsViewModel) {
        customValidatorSettingsSort.bindTo(viewModel.selectedSortingIdFlow, lifecycleScope)

        customValidatorSettingsFilterIdentity.field.bindFilter(HasIdentityFilter::class.java)
        customValidatorSettingsFilterSlashes.field.bindFilter(NotSlashedFilter::class.java)
        customValidatorSettingsFilterOverSubscribed.field.bindFilter(NotOverSubscribedFilter::class.java)
        customValidatorSettingsFilterClustering.field.bindPostProcessor(RemoveClusteringPostprocessor::class.java)

        viewModel.isResetButtonEnabled.observe(customValidatorSettingsToolbar.rightActionText::setEnabled)
        viewModel.isApplyButtonEnabled.observe {
            customValidatorSettingsApply.setState(if (it) ButtonState.NORMAL else ButtonState.DISABLED)
        }

        viewModel.tokenNameFlow.observe {
            customValidatorSettingsSortTotalStake.text = getString(R.string.staking_validator_total_stake_token, it)
            customValidatorSettingsSortOwnStake.text = getString(R.string.staking_filter_title_own_stake_token, it)
        }
    }

    private fun CompoundButton.bindPostProcessor(postProcessorClass: Class<out RecommendationPostProcessor>) {
        bindFromMap(postProcessorClass, viewModel.postProcessorsEnabledMap, lifecycleScope)
    }

    private fun CompoundButton.bindFilter(filterClass: Class<out RecommendationFilter>) {
        bindFromMap(filterClass, viewModel.filtersEnabledMap, lifecycleScope)
    }
}
