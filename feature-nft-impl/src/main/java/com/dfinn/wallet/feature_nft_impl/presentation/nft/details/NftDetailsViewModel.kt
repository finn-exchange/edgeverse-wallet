package com.dfinn.wallet.feature_nft_impl.presentation.nft.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.event
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.feature_account_api.data.mappers.mapChainToUi
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.icon.createAddressModel
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_account_api.presenatation.actions.showAddressActions
import com.dfinn.wallet.feature_nft_impl.NftRouter
import com.dfinn.wallet.feature_nft_impl.domain.nft.details.NftDetailsInteractor
import com.dfinn.wallet.feature_nft_impl.domain.nft.details.PricedNftDetails
import com.dfinn.wallet.feature_nft_impl.presentation.nft.common.formatIssuance
import com.dfinn.wallet.feature_wallet_api.presentation.model.mapAmountToAmountModel
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NftDetailsViewModel(
    private val router: NftRouter,
    private val resourceManager: ResourceManager,
    private val interactor: NftDetailsInteractor,
    private val nftIdentifier: String,
    private val externalActionsDelegate: ExternalActions.Presentation,
    private val addressIconGenerator: AddressIconGenerator,
    private val addressDisplayUseCase: AddressDisplayUseCase
) : BaseViewModel(), ExternalActions by externalActionsDelegate {

    private val _exitingErrorLiveData = MutableLiveData<Event<String>>()
    val exitingErrorLiveData: LiveData<Event<String>> = _exitingErrorLiveData

    private val nftDetailsFlow = interactor.nftDetailsFlow(nftIdentifier)
        .inBackground()
        .catch { showExitingError(it) }
        .share()

    val nftDetailsUi = nftDetailsFlow
        .map(::mapNftDetailsToUi)
        .inBackground()
        .share()

    fun ownerClicked() = launch {
        val pricedNftDetails = nftDetailsFlow.first()

        with(pricedNftDetails.nftDetails) {
            externalActionsDelegate.showAddressActions(owner, chain)
        }
    }

    fun creatorClicked() = launch {
        val pricedNftDetails = nftDetailsFlow.first()

        with(pricedNftDetails.nftDetails) {
            externalActionsDelegate.showAddressActions(creator!!, chain)
        }
    }

    private fun showExitingError(exception: Throwable) {
        _exitingErrorLiveData.value = exception.message.orEmpty().event()
    }

    private suspend fun mapNftDetailsToUi(pricedNftDetails: PricedNftDetails): NftDetailsModel {
        val nftDetails = pricedNftDetails.nftDetails

        return NftDetailsModel(
            media = nftDetails.media,
            name = nftDetails.name,
            issuance = resourceManager.formatIssuance(nftDetails.issuance),
            description = nftDetails.description,
            price = pricedNftDetails.price?.let {
                mapAmountToAmountModel(it.amount, it.token)
            },
            collection = nftDetails.collection?.let {
                NftDetailsModel.Collection(
                    name = it.name ?: it.id,
                    media = it.media,
                )
            },
            owner = createAddressModel(nftDetails.owner, nftDetails.chain),
            creator = nftDetails.creator?.let {
                createAddressModel(it, nftDetails.chain)
            },
            network = mapChainToUi(nftDetails.chain)
        )
    }

    private suspend fun createAddressModel(accountId: AccountId, chain: Chain) = addressIconGenerator.createAddressModel(
        chain = chain,
        accountId = accountId,
        sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
        addressDisplayUseCase = addressDisplayUseCase,
        background = AddressIconGenerator.BACKGROUND_TRANSPARENT
    )

    fun backClicked() {
        router.back()
    }
}
