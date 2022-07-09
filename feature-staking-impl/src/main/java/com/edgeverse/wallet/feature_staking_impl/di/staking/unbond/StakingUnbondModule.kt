package com.edgeverse.wallet.feature_staking_impl.di.staking.unbond

import dagger.Module
import dagger.Provides
import com.edgeverse.wallet.common.di.scope.FeatureScope
import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_staking_api.domain.api.EraTimeCalculatorFactory
import com.edgeverse.wallet.feature_staking_api.domain.api.StakingRepository
import com.edgeverse.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.edgeverse.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory

@Module
class StakingUnbondModule {

    @Provides
    @FeatureScope
    fun provideUnbondInteractor(
        extrinsicService: ExtrinsicService,
        stakingRepository: StakingRepository,
        eraTimeCalculatorFactory: EraTimeCalculatorFactory,
    ) = UnbondInteractor(extrinsicService, stakingRepository, eraTimeCalculatorFactory)

    @Provides
    @FeatureScope
    fun provideHintsMixinFactory(
        stakingHintsUseCase: StakingHintsUseCase
    ) = UnbondHintsMixinFactory(stakingHintsUseCase)
}
