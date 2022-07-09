package com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV2

import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.core_db.dao.NftDao
import com.edgeverse.wallet.core_db.model.NftLocal
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_account_api.domain.model.accountIdIn
import com.edgeverse.wallet.feature_account_api.domain.model.addressIn
import com.edgeverse.wallet.feature_nft_api.data.model.Nft
import com.edgeverse.wallet.feature_nft_api.data.model.NftDetails
import com.edgeverse.wallet.feature_nft_impl.data.mappers.nftIssuance
import com.edgeverse.wallet.feature_nft_impl.data.mappers.nftPrice
import com.edgeverse.wallet.feature_nft_impl.data.network.distributed.FileStorageAdapter.adoptFileStorageLinkToHttps
import com.edgeverse.wallet.feature_nft_impl.data.source.NftProvider
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV2.network.kanaria.KanariaApi
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV2.network.singular.SingularV2Api
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow

class RmrkV2NftProvider(
    private val chainRegistry: ChainRegistry,
    private val accountRepository: AccountRepository,
    private val kanariaApi: KanariaApi,
    private val singularV2Api: SingularV2Api,
    private val nftDao: NftDao
) : NftProvider {

    override suspend fun initialNftsSync(chain: Chain, metaAccount: MetaAccount, forceOverwrite: Boolean) {
        val address = metaAccount.addressIn(chain)!!
        val nfts = kanariaApi.getBirds(address) + kanariaApi.getItems(address)

        val toSave = nfts.map {
            NftLocal(
                identifier = localIdentifier(chain.id, it.id),
                metaId = metaAccount.id,
                chainId = chain.id,
                collectionId = it.collectionId,
                instanceId = null,
                metadata = it.metadata.encodeToByteArray(),
                name = it.name,
                label = it.description,
                media = it.image,
                price = it.price,
                type = NftLocal.Type.RMRK2,
                issuanceTotal = null,
                issuanceMyEdition = it.edition,
                wholeDetailsLoaded = it.image != null // null in case of items, will require metadata fetch from ipfs on full sync
            )
        }

        nftDao.insertNftsDiff(NftLocal.Type.RMRK2, metaAccount.id, toSave, forceOverwrite)
    }

    override suspend fun nftFullSync(nft: Nft) {
        val metadataLink = nft.metadataRaw!!.decodeToString().adoptFileStorageLinkToHttps()
        val metadata = kanariaApi.getIpfsMetadata(metadataLink)

        nftDao.updateNft(nft.identifier) { local ->
            local.copy(
                media = metadata.image.adoptFileStorageLinkToHttps(),
                wholeDetailsLoaded = true
            )
        }
    }

    override fun nftDetailsFlow(nftIdentifier: String): Flow<NftDetails> {
        return flowOf {
            val nftLocal = nftDao.getNft(nftIdentifier)
            require(nftLocal.wholeDetailsLoaded) {
                "Cannot load details of non fully-synced NFT"
            }
            val chain = chainRegistry.getChain(nftLocal.chainId)
            val metaAccount = accountRepository.getMetaAccount(nftLocal.metaId)

            val collection = singularV2Api.getCollection(nftLocal.collectionId).first()
            val collectionMetadata = collection.metadata?.let {
                singularV2Api.getIpfsMetadata(it.adoptFileStorageLinkToHttps())
            }

            NftDetails(
                identifier = nftLocal.identifier,
                chain = chain,
                owner = metaAccount.accountIdIn(chain)!!,
                creator = chain.accountIdOf(collection.issuer),
                media = nftLocal.media,
                name = nftLocal.name!!,
                description = nftLocal.label,
                issuance = nftIssuance(nftLocal),
                price = nftPrice(nftLocal),
                collection = NftDetails.Collection(
                    id = nftLocal.collectionId,
                    name = collectionMetadata?.name,
                    media = collectionMetadata?.image?.adoptFileStorageLinkToHttps()
                )
            )
        }
    }

    private fun localIdentifier(chainId: ChainId, remoteId: String): String {
        return "$chainId-$remoteId"
    }
}
