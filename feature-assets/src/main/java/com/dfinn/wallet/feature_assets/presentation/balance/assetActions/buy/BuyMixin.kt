package com.dfinn.wallet.feature_assets.presentation.balance.assetActions.buy

import android.view.View
import androidx.lifecycle.LiveData
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.mixin.actionAwaitable.ChooseOneOfManyAwaitable
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.feature_assets.data.buyToken.BuyTokenRegistry
import com.dfinn.wallet.feature_assets.data.buyToken.ExternalProvider
import kotlinx.coroutines.flow.Flow

interface BuyMixin {

    class IntegrationPayload(val integrator: BuyTokenRegistry.Integrator<*>)

    val awaitProviderChoosing: ChooseOneOfManyAwaitable<BuyProvider>

    val integrateWithBuyProviderEvent: LiveData<Event<IntegrationPayload>>

    val buyEnabled: Flow<Boolean>

    fun buyClicked()

    interface Presentation : BuyMixin
}

fun BaseFragment<*>.setupBuyIntegration(
    mixin: BuyMixin,
    buyButton: View,
) {
    mixin.integrateWithBuyProviderEvent.observeEvent {
        with(it) {
            when (integrator) {
                is ExternalProvider.Integrator -> integrator.openBuyFlow(requireContext())
            }
        }
    }

    mixin.awaitProviderChoosing.awaitableActionLiveData.observeEvent { action ->
        BuyProviderChooserBottomSheet(
            context = requireContext(),
            payload = action.payload,
            onSelect = action.onSuccess,
            onCancel = action.onCancel
        ).show()
    }

    buyButton.setOnClickListener { mixin.buyClicked() }

    mixin.buyEnabled.observe(buyButton::setEnabled)
}
