package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main

import android.os.Parcelable
import com.edgeverse.wallet.common.address.AddressIconGenerator
import com.edgeverse.wallet.common.address.AddressModel
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import com.edgeverse.wallet.feature_account_api.data.mappers.mapChainToUi
import com.edgeverse.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.edgeverse.wallet.feature_account_api.presenatation.chain.ChainUi
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.ParachainMetadata
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.CrossChainRewardDestination
import com.edgeverse.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.ConfirmContributeCustomization
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.SelectContributeCustomization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class MoonbeamRewardDestinationUi(
    val addressModel: AddressModel,
    val chain: ChainUi,
    val title: String,
)

class MoonbeamMainFlowCustomViewStateFactory(
    private val interactor: MoonbeamCrowdloanInteractor,
    private val resourceManager: ResourceManager,
    private val iconGenerator: AddressIconGenerator,
) {

    fun create(scope: CoroutineScope, parachainMetadata: ParachainMetadata): MoonbeamMainFlowCustomViewState {
        return MoonbeamMainFlowCustomViewState(scope, parachainMetadata, interactor, resourceManager, iconGenerator)
    }
}

class MoonbeamMainFlowCustomViewState(
    coroutineScope: CoroutineScope,
    private val parachainMetadata: ParachainMetadata,
    interactor: MoonbeamCrowdloanInteractor,
    private val resourceManager: ResourceManager,
    private val iconGenerator: AddressIconGenerator,
) :
    SelectContributeCustomization.ViewState,
    ConfirmContributeCustomization.ViewState,
    CoroutineScope by coroutineScope {

    val moonbeamRewardDestination = flowOf { interactor.getMoonbeamRewardDestination(parachainMetadata) }
        .map(::mapMoonbeamChainDestinationToUi)
        .inBackground()
        .shareIn(this, started = SharingStarted.Eagerly, replay = 1)

    private suspend fun mapMoonbeamChainDestinationToUi(crossChainRewardDestination: CrossChainRewardDestination): MoonbeamRewardDestinationUi {
        return MoonbeamRewardDestinationUi(
            addressModel = iconGenerator.createAddressModel(
                chain = crossChainRewardDestination.destination,
                address = crossChainRewardDestination.addressInDestination,
                sizeInDp = AddressIconGenerator.SIZE_SMALL,
                accountName = null
            ),
            chain = mapChainToUi(crossChainRewardDestination.destination),
            title = resourceManager.getString(R.string.crowdloan_moonbeam_reward_destination, parachainMetadata.token)
        )
    }

    override val customizationPayloadFlow: Flow<Parcelable?> = kotlinx.coroutines.flow.flowOf(null)
}
