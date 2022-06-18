package com.dfinn.wallet.feature_staking_impl.di.staking.unbond

import dagger.Module
import dagger.Provides
import com.dfinn.wallet.common.di.scope.FeatureScope
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_staking_api.domain.api.EraTimeCalculatorFactory
import com.dfinn.wallet.feature_staking_api.domain.api.StakingRepository
import com.dfinn.wallet.feature_staking_impl.domain.staking.unbond.UnbondInteractor
import com.dfinn.wallet.feature_staking_impl.presentation.common.hints.StakingHintsUseCase
import com.dfinn.wallet.feature_staking_impl.presentation.staking.unbond.hints.UnbondHintsMixinFactory

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
