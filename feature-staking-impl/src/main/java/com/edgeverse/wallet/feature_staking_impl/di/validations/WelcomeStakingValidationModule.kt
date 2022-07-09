package com.edgeverse.wallet.feature_staking_impl.di.validations

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.common.validation.CompositeValidation
import com.edgeverse.wallet.common.validation.ValidationSystem
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingMaxNominatorsValidation
import com.edgeverse.wallet.feature_staking_impl.domain.validations.welcome.WelcomeStakingValidationFailure

@Module
class WelcomeStakingValidationModule {

    @Provides
    @FeatureScope
    fun provideMaxNominatorsReachedValidation(
        stakingSharedState: StakingSharedState,
        stakingRepository: StakingRepository
    ) = WelcomeStakingMaxNominatorsValidation(
        stakingRepository = stakingRepository,
        errorProducer = { WelcomeStakingValidationFailure.MAX_NOMINATORS_REACHED },
        isAlreadyNominating = { false },
        sharedState = stakingSharedState
    )

    @Provides
    @FeatureScope
    fun provideSetupStakingValidationSystem(
        maxNominatorsReachedValidation: WelcomeStakingMaxNominatorsValidation
    ) = ValidationSystem(
        CompositeValidation(
            listOf(
                maxNominatorsReachedValidation,
            )
        )
    )
}
