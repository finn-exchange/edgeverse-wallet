package com.edgeverse.wallet.feature_assets.presentation.balance.assetActions.buy

import androidx.lifecycle.MutableLiveData
import com.edgeverse.wallet.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.edgeverse.wallet.common.mixin.actionAwaitable.selectingOneOf
import com.edgeverse.wallet.common.utils.Event
import com.edgeverse.wallet.common.utils.WithCoroutineScopeExtensions
import com.edgeverse.wallet.common.utils.event
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.invoke
import com.edgeverse.wallet.common.utils.lazyAsync
import com.edgeverse.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.domain.model.addressIn
import com.edgeverse.wallet.feature_assets.data.buyToken.BuyTokenRegistry
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chainWithAsset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BuyMixinFactory(
    private val buyTokenRegistry: BuyTokenRegistry,
    private val chainRegistry: ChainRegistry,
    private val accountUseCase: SelectedAccountUseCase,
    private val awaitableMixinFactory: ActionAwaitableMixin.Factory
) {

    fun create(
        scope: CoroutineScope,
        assetPayload: AssetPayload,
    ): BuyMixin.Presentation {
        return BuyMixinProvider(
            buyTokenRegistry = buyTokenRegistry,
            chainRegistry = chainRegistry,
            accountUseCase = accountUseCase,
            coroutineScope = scope,
            assetPayload = assetPayload,
            actionAwaitableMixinFactory = awaitableMixinFactory
        )
    }
}

private class BuyMixinProvider(
    private val buyTokenRegistry: BuyTokenRegistry,
    private val chainRegistry: ChainRegistry,
    private val accountUseCase: SelectedAccountUseCase,
    actionAwaitableMixinFactory: ActionAwaitableMixin.Factory,

    coroutineScope: CoroutineScope,
    private val assetPayload: AssetPayload,
) : BuyMixin.Presentation,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    private val chainWithAssetAsync by lazyAsync { chainRegistry.chainWithAsset(assetPayload.chainId, assetPayload.chainAssetId) }

    override val awaitProviderChoosing = actionAwaitableMixinFactory.selectingOneOf<BuyProvider>()

    override val integrateWithBuyProviderEvent = MutableLiveData<Event<BuyMixin.IntegrationPayload>>()

    override val buyEnabled: Flow<Boolean> = flowOf {
        val (_, chainAsset) = chainWithAssetAsync()

        buyTokenRegistry.availableProvidersFor(chainAsset).isNotEmpty()
    }

    override fun buyClicked() {
        launch {
            val (_, chainAsset) = chainWithAssetAsync()
            val availableProviders = buyTokenRegistry.availableProvidersFor(chainAsset)

            when {
                availableProviders.isEmpty() -> return@launch
                availableProviders.size == 1 -> openProvider(availableProviders.single())
                else -> {
                    val payload = DynamicListBottomSheet.Payload(availableProviders)
                    val provider = awaitProviderChoosing.awaitAction(payload)

                    openProvider(provider)
                }
            }
        }
    }

    private suspend fun openProvider(provider: BuyTokenRegistry.Provider<*>) {
        val (chain, chainAsset) = chainWithAssetAsync()
        val address = accountUseCase.getSelectedMetaAccount().addressIn(chain)!!

        val integrator = provider.createIntegrator(chainAsset, address)

        integrateWithBuyProviderEvent.value = BuyMixin.IntegrationPayload(integrator).event()
    }
}
