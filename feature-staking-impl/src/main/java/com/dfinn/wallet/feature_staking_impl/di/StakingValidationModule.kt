package com.dfinn.wallet.feature_staking_impl.di

import com.dfinn.wallet.feature_staking_impl.di.validations.*
import dagger.Module

@Module(
    includes = [
        MakePayoutValidationsModule::class,
        SetupStakingValidationsModule::class,
        BondMoreValidationsModule::class,
        UnbondValidationsModule::class,
        RedeemValidationsModule::class,
        RebondValidationsModule::class,
        SetControllerValidationsModule::class,
        RewardDestinationValidationsModule::class,
        WelcomeStakingValidationModule::class,
        StakeActionsValidationModule::class
    ]
)
class StakingValidationModule
