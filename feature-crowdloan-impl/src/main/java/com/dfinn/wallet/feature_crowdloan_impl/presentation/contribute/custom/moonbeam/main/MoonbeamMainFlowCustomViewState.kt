package com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.main

import android.os.Parcelable
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.flowOf
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.feature_account_api.data.mappers.mapChainToUi
import com.dfinn.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.dfinn.wallet.feature_account_api.presenatation.chain.ChainUi
import com.dfinn.wallet.feature_crowdloan_api.data.repository.ParachainMetadata
import com.dfinn.wallet.feature_crowdloan_impl.R
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.CrossChainRewardDestination
import com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam.MoonbeamCrowdloanInteractor
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.ConfirmContributeCustomization
import com.dfinn.wallet.feature_crowdloan_impl.presentation.contribute.custom.SelectContributeCustomization
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
