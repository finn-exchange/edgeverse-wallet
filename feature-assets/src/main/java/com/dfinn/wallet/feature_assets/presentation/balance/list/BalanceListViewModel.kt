package com.dfinn.wallet.feature_assets.presentation.balance.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.address.AddressIconGenerator
import com.dfinn.wallet.common.address.createAddressModel
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.list.toListWithHeaders
import com.dfinn.wallet.common.presentation.LoadingState
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.utils.format
import com.dfinn.wallet.common.utils.formatAsCurrency
import com.dfinn.wallet.common.utils.inBackground
import com.dfinn.wallet.feature_account_api.data.mappers.mapChainToUi
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import com.dfinn.wallet.feature_account_api.domain.model.defaultSubstrateAddress
import com.dfinn.wallet.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import com.dfinn.wallet.feature_assets.domain.WalletInteractor
import com.dfinn.wallet.feature_assets.domain.assets.list.AssetsListInteractor
import com.dfinn.wallet.feature_assets.presentation.AssetPayload
import com.dfinn.wallet.feature_assets.presentation.WalletRouter
import com.dfinn.wallet.feature_assets.presentation.balance.list.model.AssetGroupUi
import com.dfinn.wallet.feature_assets.presentation.balance.list.model.NftPreviewUi
import com.dfinn.wallet.feature_assets.presentation.balance.list.model.TotalBalanceModel
import com.dfinn.wallet.feature_assets.presentation.model.AssetModel
import com.dfinn.wallet.feature_nft_api.data.model.Nft
import com.dfinn.wallet.feature_wallet_api.domain.model.AssetGroup
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

private const val CURRENT_ICON_SIZE = 40

private typealias SyncAction = suspend (MetaAccount) -> Unit

@OptIn(ExperimentalTime::class)
class BalanceListViewModel(
    private val interactor: WalletInteractor,
    private val assetsListInteractor: AssetsListInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
) : BaseViewModel() {

    private val _hideRefreshEvent = MutableLiveData<Event<Unit>>()
    val hideRefreshEvent: LiveData<Event<Unit>> = _hideRefreshEvent

    private val fullSyncActions: List<SyncAction> = listOf(
        { interactor.syncAssetsRates() },
        interactor::syncNfts
    )

    private val accountChangeSyncActions: List<SyncAction> = listOf(
        interactor::syncNfts
    )

    private val selectedMetaAccount = selectedAccountUseCase.selectedMetaAccountFlow()
        .share()

    val currentAddressModelFlow = selectedMetaAccount
        .map { addressIconGenerator.createAddressModel(it.defaultSubstrateAddress, CURRENT_ICON_SIZE, it.name) }
        .inBackground()
        .share()

    private val nftsPreviews = assetsListInteractor.observeNftPreviews()
        .inBackground()
        .share()

    val nftCountFlow = nftsPreviews
        .map { it.totalNftsCount.format() }
        .inBackground()
        .share()

    val nftPreviewsUi = nftsPreviews
        .map { it.nftPreviews.map(::mapNftPreviewToUi) }
        .inBackground()
        .share()

    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()

    val assetsFlow = balancesFlow.map { balances ->
        balances.assets
            .mapKeys { (assetGroup, _) -> mapAssetGroupToUi(assetGroup) }
            .mapValues { (_, assets) -> assets.map(::mapAssetToAssetModel) }
            .toListWithHeaders()
    }
        .distinctUntilChanged()
        .inBackground()
        .share()

    val totalBalanceFlow = balancesFlow.map {
        TotalBalanceModel(
            shouldShowPlaceholder = it.assets.isEmpty(),
            totalBalanceFiat = it.totalBalanceFiat.formatAsCurrency(),
            lockedBalanceFiat = it.lockedBalanceFiat.formatAsCurrency()
        )
    }
        .inBackground()
        .share()

    init {
        fullSync()

        nftsPreviews
            .debounce(1L.seconds)
            .onEach { nfts ->
                nfts.nftPreviews
                    .filter { it.details is Nft.Details.Loadable }
                    .forEach { assetsListInteractor.fullSyncNft(it) }
            }
            .inBackground()
            .launchIn(this)

        selectedMetaAccount
            .mapLatest { syncWith(accountChangeSyncActions, it) }
            .launchIn(this)
    }

    fun fullSync() {
        viewModelScope.launch {
            syncWith(fullSyncActions, selectedMetaAccount.first())

            _hideRefreshEvent.value = Event(Unit)
        }
    }

    fun assetClicked(asset: AssetModel) {
        val payload = AssetPayload(
            chainId = asset.token.configuration.chainId,
            chainAssetId = asset.token.configuration.id
        )

        router.openAssetDetails(payload)
    }

    fun avatarClicked() {
        router.openChangeAccount()
    }

    fun manageClicked() {
        router.openAssetFilters()
    }

    fun goToNftsClicked() {
        router.openNfts()
    }

    private suspend fun syncWith(syncActions: List<SyncAction>, metaAccount: MetaAccount) = if (syncActions.size == 1) {
        val syncAction = syncActions.first()
        syncAction(metaAccount)
    } else {
        val syncJobs = syncActions.map { async { it(metaAccount) } }
        syncJobs.joinAll()
    }

    private fun mapNftPreviewToUi(nftPreview: Nft): NftPreviewUi {
        return when (val details = nftPreview.details) {
            Nft.Details.Loadable -> LoadingState.Loading()
            is Nft.Details.Loaded -> LoadingState.Loaded(details.metadata?.media)
        }
    }

    private fun mapAssetGroupToUi(assetGroup: AssetGroup) = AssetGroupUi(
        chainUi = mapChainToUi(assetGroup.chain),
        groupBalanceFiat = assetGroup.groupBalanceFiat.formatAsCurrency()
    )
}
