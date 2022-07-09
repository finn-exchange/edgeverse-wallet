package com.edgeverse.wallet.feature_staking_impl.di

import dagger.BindsInstance
import dagger.Component
import com.edgeverse.wallet.common.di.CommonApi
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.core_db.di.DbApi
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.di.staking.unbond.StakingUnbondModule
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.di.ConfirmStakingComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations.di.ConfirmNominationsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.di.ConfirmPayoutComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.detail.di.PayoutDetailsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.list.di.PayoutsListComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.setup.di.SetupStakingComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.di.ConfirmBondMoreComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select.di.SelectBondMoreComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm.di.ConfirmSetControllerComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.set.di.SetControllerComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.di.StakingComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.di.ConfirmRebondComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.custom.di.CustomRebondComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem.di.RedeemComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.di.ConfirmRewardDestinationComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.select.di.SelectRewardDestinationComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.di.ConfirmUnbondComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.select.di.SelectUnbondComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.story.di.StoryComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.review.di.ReviewCustomValidatorsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.search.di.SearchCustomValidatorsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.select.di.SelectCustomValidatorsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.custom.settings.di.CustomValidatorsSettingsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.recommended.di.RecommendedValidatorsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.start.di.StartChangeValidatorsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.current.di.CurrentValidatorsComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.details.di.ValidatorDetailsComponent
import com.edgeverse.wallet.feature_wallet_api.di.WalletFeatureApi
import com.edgeverse.wallet.runtime.di.RuntimeApi

@Component(
    dependencies = [
        StakingFeatureDependencies::class
    ],
    modules = [
        StakingFeatureModule::class,
        StakingUpdatersModule::class,
        StakingValidationModule::class,
        StakingUnbondModule::class
    ]
)
@FeatureScope
interface StakingFeatureComponent : StakingFeatureApi {

    fun searchCustomValidatorsComponentFactory(): SearchCustomValidatorsComponent.Factory

    fun customValidatorsSettingsComponentFactory(): CustomValidatorsSettingsComponent.Factory

    fun reviewCustomValidatorsComponentFactory(): ReviewCustomValidatorsComponent.Factory

    fun selectCustomValidatorsComponentFactory(): SelectCustomValidatorsComponent.Factory

    fun startChangeValidatorsComponentFactory(): StartChangeValidatorsComponent.Factory

    fun recommendedValidatorsComponentFactory(): RecommendedValidatorsComponent.Factory

    fun stakingComponentFactory(): StakingComponent.Factory

    fun setupStakingComponentFactory(): SetupStakingComponent.Factory

    fun confirmStakingComponentFactory(): ConfirmStakingComponent.Factory

    fun confirmNominationsComponentFactory(): ConfirmNominationsComponent.Factory

    fun validatorDetailsComponentFactory(): ValidatorDetailsComponent.Factory

    fun storyComponentFactory(): StoryComponent.Factory

    fun payoutsListFactory(): PayoutsListComponent.Factory

    fun payoutDetailsFactory(): PayoutDetailsComponent.Factory

    fun confirmPayoutFactory(): ConfirmPayoutComponent.Factory

    fun selectBondMoreFactory(): SelectBondMoreComponent.Factory

    fun confirmBondMoreFactory(): ConfirmBondMoreComponent.Factory

    fun selectUnbondFactory(): SelectUnbondComponent.Factory

    fun confirmUnbondFactory(): ConfirmUnbondComponent.Factory

    fun redeemFactory(): RedeemComponent.Factory

    fun confirmRebondFactory(): ConfirmRebondComponent.Factory

    fun setControllerFactory(): SetControllerComponent.Factory

    fun confirmSetControllerFactory(): ConfirmSetControllerComponent.Factory

    fun rebondCustomFactory(): CustomRebondComponent.Factory

    fun currentValidatorsFactory(): CurrentValidatorsComponent.Factory

    fun selectRewardDestinationFactory(): SelectRewardDestinationComponent.Factory

    fun confirmRewardDestinationFactory(): ConfirmRewardDestinationComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance router: StakingRouter,
            deps: StakingFeatureDependencies
        ): StakingFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            RuntimeApi::class,
            AccountFeatureApi::class,
            WalletFeatureApi::class
        ]
    )
    interface StakingFeatureDependenciesComponent : StakingFeatureDependencies
}
