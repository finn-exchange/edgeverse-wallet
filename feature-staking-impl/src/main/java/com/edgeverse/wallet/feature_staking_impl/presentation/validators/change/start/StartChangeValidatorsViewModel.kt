package com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.start

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.data.network.AppLinksProvider
import com.edgeverse.wallet.common.mixin.api.Browserable
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.domain.StakingInteractor
import com.edgeverse.wallet.feature_staking_impl.domain.recommendations.ValidatorRecommendatorFactory
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingProcess
import com.edgeverse.wallet.feature_staking_impl.presentation.common.SetupStakingSharedState
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.retractValidators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class Texts(
    val toolbarTitle: String,
    val selectManuallyTitle: String,
    val selectManuallyBadge: String?
)

class StartChangeValidatorsViewModel(
    private val router: StakingRouter,
    private val validatorRecommendatorFactory: ValidatorRecommendatorFactory,
    private val setupStakingSharedState: SetupStakingSharedState,
    private val appLinksProvider: AppLinksProvider,
    private val resourceManager: ResourceManager,
    private val interactor: StakingInteractor,
) : BaseViewModel(), Browserable {

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    private val maxValidatorsPerNominator = flowOf {
        interactor.maxValidatorsPerNominator()
    }.share()

    val validatorsLoading = MutableStateFlow(true)

    val customValidatorsTexts = setupStakingSharedState.setupStakingProcess.transform {
        when {
            it is SetupStakingProcess.ReadyToSubmit && it.payload.validators.isNotEmpty() -> emit(
                Texts(
                    toolbarTitle = resourceManager.getString(R.string.staking_change_validators),
                    selectManuallyTitle = resourceManager.getString(R.string.staking_select_custom),
                    selectManuallyBadge = resourceManager.getString(
                        R.string.staking_max_format,
                        it.payload.validators.size,
                        maxValidatorsPerNominator.first()
                    )
                )
            )
            else -> emit(
                Texts(
                    toolbarTitle = resourceManager.getString(R.string.staking_set_validators),
                    selectManuallyTitle = resourceManager.getString(R.string.staking_select_custom),
                    selectManuallyBadge = null
                )
            )
        }
    }

    init {
        launch {
            validatorRecommendatorFactory.awaitValidatorLoading(router.currentStackEntryLifecycle)

            validatorsLoading.value = false
        }
    }

    fun goToCustomClicked() {
        router.openSelectCustomValidators()
    }

    fun goToRecommendedClicked() {
        router.openRecommendedValidators()
    }

    fun backClicked() {
        setupStakingSharedState.retractValidators()

        router.back()
    }

    fun recommendedLearnMoreClicked() {}
}
