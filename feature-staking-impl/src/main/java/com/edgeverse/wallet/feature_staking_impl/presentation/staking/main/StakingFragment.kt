package com.edgeverse.wallet.feature_staking_impl.presentation.staking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeValidations
import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.view.dialog.infoDialog
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_staking_impl.domain.model.NominatorStatus
import com.edgeverse.wallet.feature_staking_impl.domain.model.StashNoneStatus
import com.edgeverse.wallet.feature_staking_impl.domain.model.ValidatorStatus
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.model.StakingNetworkInfoModel
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.unbonding.setupUnbondingMixin
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.view.ManageStakingView
import com.edgeverse.wallet.feature_staking_impl.presentation.view.StakeSummaryView
import com.edgeverse.wallet.feature_wallet_api.presentation.mixin.assetSelector.setupAssetSelector
import kotlinx.android.synthetic.main.fragment_staking.stakingAlertsInfo
import kotlinx.android.synthetic.main.fragment_staking.stakingAssetSelector
import kotlinx.android.synthetic.main.fragment_staking.stakingAvatar
import kotlinx.android.synthetic.main.fragment_staking.stakingContainer
import kotlinx.android.synthetic.main.fragment_staking.stakingEstimate
import kotlinx.android.synthetic.main.fragment_staking.stakingNetworkInfo
import kotlinx.android.synthetic.main.fragment_staking.stakingStakeManage
import kotlinx.android.synthetic.main.fragment_staking.stakingStakeSummary
import kotlinx.android.synthetic.main.fragment_staking.stakingStakeUnbondings
import kotlinx.android.synthetic.main.fragment_staking.stakingUserRewards
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class StakingFragment : BaseFragment<StakingViewModel>() {

    @Inject protected lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_staking, container, false)
    }

    override fun initViews() {
        stakingContainer.applyStatusBarInsets()

        stakingAvatar.setOnClickListener {
            viewModel.avatarClicked()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .stakingComponentFactory()
            .create(this)
            .inject(this)
    }

    @ExperimentalTime
    override fun subscribe(viewModel: StakingViewModel) {
        observeValidations(viewModel)
        setupAssetSelector(stakingAssetSelector, viewModel, imageLoader)

        viewModel.alertsFlow.observe { loadingState ->
            when (loadingState) {
                is LoadingState.Loaded -> {
                    stakingAlertsInfo.hideLoading()

                    if (loadingState.data.isEmpty()) {
                        stakingAlertsInfo.makeGone()
                    } else {
                        stakingAlertsInfo.makeVisible()
                        stakingAlertsInfo.setStatus(loadingState.data)
                    }
                }

                is LoadingState.Loading -> {
                    stakingAlertsInfo.makeVisible()
                    stakingAlertsInfo.showLoading()
                }
            }
        }

        viewModel.stakingViewStateFlow.observe { loadingState ->
            when (loadingState) {
                is LoadingState.Loading -> {
                    stakingEstimate.setVisible(false)
                    stakingUserRewards.setVisible(false)
                    stakingStakeSummary.setVisible(false)
                    stakingStakeUnbondings.setVisible(false)
                }

                is LoadingState.Loaded -> {
                    val stakingState = loadingState.data

                    stakingEstimate.setVisible(stakingState is WelcomeViewState)
                    stakingUserRewards.setVisible(stakingState is StakeViewState<*>)
                    stakingStakeSummary.setVisible(stakingState is StakeViewState<*>)
                    stakingStakeManage.setVisible(stakingState is StakeViewState<*>)

                    if (stakingState !is StakeViewState<*>) stakingStakeUnbondings.makeGone()

                    stakingNetworkInfo.setExpanded(stakingState is WelcomeViewState)

                    observeValidations(stakingState)

                    when (stakingState) {
                        is NominatorViewState -> bindStashViews(stakingState, ::mapNominatorStatus)

                        is ValidatorViewState -> bindStashViews(stakingState, ::mapValidatorStatus)

                        is StashNoneViewState -> bindStashViews(stakingState, ::mapStashNoneStatus)

                        is WelcomeViewState -> {

                            stakingState.estimateEarningsTitle.observe(stakingEstimate::setTitle)

                            stakingState.returns.observe { rewardsState ->
                                when (rewardsState) {
                                    is LoadingState.Loaded -> {
                                        val rewards = rewardsState.data

                                        stakingEstimate.showGains(rewards.monthlyPercentage, rewards.yearlyPercentage)
                                    }

                                    is LoadingState.Loading -> stakingEstimate.showLoading()
                                }
                            }

                            stakingEstimate.startStakingButton.setOnClickListener { stakingState.nextClicked() }

                            stakingEstimate.infoActions.setOnClickListener { stakingState.infoActionClicked() }

                            stakingState.showRewardEstimationEvent.observeEvent {
                                StakingRewardEstimationBottomSheet(requireContext(), it).show()
                            }
                        }
                    }
                }
            }
        }

        viewModel.networkInfoStateLiveData.observe { state ->
            when (state) {
                is LoadingState.Loading<*> -> stakingNetworkInfo.showLoading()

                is LoadingState.Loaded<StakingNetworkInfoModel> -> with(state.data) {
                    with(stakingNetworkInfo) {
                        setTotalStaked(totalStaked)
                        setNominatorsCount(activeNominators)
                        setMinimumStake(minimumStake)
                        setUnstakingPeriod(unstakingPeriod)
                        setStakingPeriod(stakingPeriod)
                    }
                }
            }
        }

        viewModel.currentAddressModelLiveData.observe {
            stakingAvatar.setImageDrawable(it.image)
        }
    }

    private fun <S> bindStashViews(
        stakingViewState: StakeViewState<S>,
        mapStatus: (StakeSummaryModel<S>) -> StakeSummaryView.Status,
    ) {
        bindUserRewards(stakingViewState)

        stakingStakeSummary.bindStakeSummary(stakingViewState, mapStatus)
        stakingStakeManage.bindStakeActions(stakingViewState)

        setupUnbondingMixin(stakingViewState.unbondingMixin, stakingStakeUnbondings)
    }

    private fun bindUserRewards(
        stakingViewState: StakeViewState<*>
    ) {
        stakingViewState.userRewardsFlow.observe {
            when (it) {
                is LoadingState.Loaded -> stakingUserRewards.showValue(it.data)
                is LoadingState.Loading -> stakingUserRewards.showLoading()
            }
        }
    }

    private fun ManageStakingView.bindStakeActions(
        stakingViewState: StakeViewState<*>
    ) {
        with(stakingViewState.manageStakeMixin) {
            setAvailableActions(allowedStakeActions)
            onManageStakeActionClicked(::manageActionChosen)
        }
    }

    private fun <S> StakeSummaryView.bindStakeSummary(
        stakingViewState: StakeViewState<S>,
        mapStatus: (StakeSummaryModel<S>) -> StakeSummaryView.Status,
    ) {
        setStatusClickListener {
            stakingViewState.statusClicked()
        }

        stakingViewState.showStatusAlertEvent.observeEvent { (title, message) ->
            showStatusAlert(title, message)
        }

        stakingViewState.stakeSummaryFlow.observe { summaryState ->
            when (summaryState) {
                is LoadingState.Loaded<StakeSummaryModel<S>> -> {
                    val summary = summaryState.data

                    showStakeAmount(summary.totalStaked)
                    showStakeStatus(mapStatus(summary))
                }
                is LoadingState.Loading -> showLoading()
            }
        }
    }

    private fun showStatusAlert(title: String, message: String) {
        infoDialog(requireContext()) {
            setTitle(title)
            setMessage(message)
        }
    }

    private fun mapNominatorStatus(summary: NominatorSummaryModel): StakeSummaryView.Status {
        return when (summary.status) {
            is NominatorStatus.Inactive -> StakeSummaryView.Status.Inactive
            NominatorStatus.Active -> StakeSummaryView.Status.Active
            is NominatorStatus.Waiting -> StakeSummaryView.Status.Waiting(summary.status.timeLeft)
        }
    }

    private fun mapValidatorStatus(summary: ValidatorSummaryModel): StakeSummaryView.Status {
        return when (summary.status) {
            ValidatorStatus.INACTIVE -> StakeSummaryView.Status.Inactive
            ValidatorStatus.ACTIVE -> StakeSummaryView.Status.Active
        }
    }

    private fun mapStashNoneStatus(summary: StashNoneSummaryModel): StakeSummaryView.Status {
        return when (summary.status) {
            StashNoneStatus.INACTIVE -> StakeSummaryView.Status.Inactive
        }
    }
}
