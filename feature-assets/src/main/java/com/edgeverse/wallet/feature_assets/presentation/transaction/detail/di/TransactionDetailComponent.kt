package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.extrinsic.ExtrinsicDetailFragment
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.reward.RewardDetailFragment
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.transfer.TransferDetailFragment

@Subcomponent(
    modules = [
        TransactionDetailModule::class,
    ]
)
@ScreenScope
interface TransactionDetailComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance operation: OperationParcelizeModel.Transfer
        ): TransactionDetailComponent
    }

    fun inject(fragment: TransferDetailFragment)
}

@Subcomponent(
    modules = [
        RewardDetailModule::class
    ]
)
@ScreenScope
interface RewardDetailComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance operation: OperationParcelizeModel.Reward
        ): RewardDetailComponent
    }

    fun inject(fragment: RewardDetailFragment)
}

@Subcomponent(
    modules = [
        ExtrinsicDetailModule::class
    ]
)
@ScreenScope
interface ExtrinsicDetailComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance extrinsic: OperationParcelizeModel.Extrinsic
        ): ExtrinsicDetailComponent
    }

    fun inject(fragment: ExtrinsicDetailFragment)
}
