package com.dfinn.wallet.feature_staking_impl.presentation.common.rewardDestination

import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.common.mixin.api.Browserable
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.invoke
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.dfinn.wallet.feature_staking_api.domain.model.StakingState
import com.dfinn.wallet.feature_staking_impl.domain.rewards.RewardCalculator
import com.dfinn.wallet.feature_wallet_api.domain.model.Asset
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.amountChooser.AmountChooserMixin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import java.math.BigDecimal

interface RewardDestinationMixin : Browserable {

    val rewardReturnsLiveData: LiveData<RewardDestinationEstimations>

    val showDestinationChooserEvent: LiveData<Event<DynamicListBottomSheet.Payload<AddressModel>>>

    val rewardDestinationModelFlow: Flow<RewardDestinationModel>

    fun payoutClicked(scope: CoroutineScope)

    fun payoutTargetClicked(scope: CoroutineScope)

    fun payoutDestinationChanged(newDestination: AddressModel)

    fun learnMoreClicked()

    fun restakeClicked()

    interface Presentation : RewardDestinationMixin {

        val rewardDestinationChangedFlow: Flow<Boolean>

        suspend fun loadActiveRewardDestination(stashState: StakingState.Stash)

        suspend fun updateReturns(
            rewardCalculator: RewardCalculator,
            asset: Asset,
            amount: BigDecimal,
        )
    }
}

fun RewardDestinationMixin.Presentation.connectWith(
    amountChooserMixin: AmountChooserMixin.Presentation,
    rewardCalculator: Deferred<RewardCalculator>
) {
    amountChooserMixin.usedAssetFlow.combine(amountChooserMixin.amount) { asset, amount ->
        updateReturns(rewardCalculator(), asset, amount)
    }.launchIn(scope = amountChooserMixin)
}
