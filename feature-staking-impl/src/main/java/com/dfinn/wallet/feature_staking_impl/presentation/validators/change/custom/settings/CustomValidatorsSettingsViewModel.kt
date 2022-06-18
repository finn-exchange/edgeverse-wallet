package com.dfinn.wallet.feature_staking_impl.presentation.validators.change.custom.settings

import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.common.utils.invoke
import com.dfinn.wallet.common.utils.lazyAsync
import com.dfinn.wallet.common.utils.reversed
import com.dfinn.wallet.feature_staking_impl.R
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSettings
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.RecommendationSettingsProviderFactory
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.filters.HasIdentityFilter
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.filters.NotOverSubscribedFilter
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.filters.NotSlashedFilter
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.postprocessors.RemoveClusteringPostprocessor
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.sortings.APYSorting
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.sortings.TotalStakeSorting
import com.dfinn.wallet.feature_staking_impl.domain.recommendations.settings.sortings.ValidatorOwnStakeSorting
import com.dfinn.wallet.feature_staking_impl.presentation.StakingRouter
import com.dfinn.wallet.feature_wallet_api.domain.TokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val SORT_MAPPING = mapOf(
    R.id.customValidatorSettingsSortAPY to APYSorting,
    R.id.customValidatorSettingsSortTotalStake to TotalStakeSorting,
    R.id.customValidatorSettingsSortOwnStake to ValidatorOwnStakeSorting
)

private val SORT_MAPPING_REVERSE = SORT_MAPPING.reversed()

class CustomValidatorsSettingsViewModel(
    private val router: StakingRouter,
    private val recommendationSettingsProviderFactory: RecommendationSettingsProviderFactory,
    private val tokenUseCase: TokenUseCase
) : BaseViewModel() {

    private val recommendationSettingsProvider by lazyAsync {
        recommendationSettingsProviderFactory.create(router.currentStackEntryLifecycle)
    }

    val selectedSortingIdFlow = MutableStateFlow(R.id.customValidatorSettingsSortAPY)

    private val initialSettingsFlow = flow { emit(recommendationSettingsProvider().currentSettings()) }
        .share()

    private val defaultSettingsFlow = flow { emit(recommendationSettingsProvider().defaultSelectCustomSettings()) }
        .share()

    val filtersEnabledMap = createClassEnabledMap(
        HasIdentityFilter::class.java,
        NotOverSubscribedFilter::class.java,
        NotSlashedFilter::class.java
    )

    val postProcessorsEnabledMap = createClassEnabledMap(
        RemoveClusteringPostprocessor::class.java
    )

    private val modifiedSettings = combine(
        filtersEnabledMap.values + postProcessorsEnabledMap.values + selectedSortingIdFlow
    ) {
        recommendationSettingsProvider().createModifiedCustomValidatorsSettings(
            filterIncluder = { filtersEnabledMap.checkEnabled(it::class.java) },
            postProcessorIncluder = { postProcessorsEnabledMap.checkEnabled(it::class.java) },
            sorting = SORT_MAPPING.getValue(selectedSortingIdFlow.value)
        )
    }.inBackground()
        .share()

    val tokenNameFlow = tokenUseCase.currentTokenFlow().map { it.configuration.symbol }

    val isApplyButtonEnabled = combine(initialSettingsFlow, modifiedSettings) { initial, modified ->
        initial != modified
    }.share()

    val isResetButtonEnabled = combine(defaultSettingsFlow, modifiedSettings) { default, modified ->
        default != modified
    }

    init {
        viewModelScope.launch {
            initFromSettings(initialSettingsFlow.first())
        }
    }

    private fun initFromSettings(currentSettings: RecommendationSettings) {
        currentSettings.customEnabledFilters.forEach {
            filtersEnabledMap[it::class.java]?.value = true
        }

        currentSettings.postProcessors.forEach {
            postProcessorsEnabledMap[it::class.java]?.value = true
        }

        selectedSortingIdFlow.value = SORT_MAPPING_REVERSE[currentSettings.sorting]!!
    }

    fun reset() {
        viewModelScope.launch {
            val defaultSettings = recommendationSettingsProvider().defaultSelectCustomSettings()

            initFromSettings(defaultSettings)
        }
    }

    fun applyChanges() {
        viewModelScope.launch {
            recommendationSettingsProvider().setCustomValidatorsSettings(modifiedSettings.first())

            router.back()
        }
    }

    fun backClicked() {
        router.back()
    }

    private fun <T> createClassEnabledMap(vararg classes: Class<out T>) = classes.associate {
        it to MutableStateFlow(false)
    }

    private fun <T> Map<out T, MutableStateFlow<Boolean>>.checkEnabled(key: T) = get(key)?.value ?: false
}
