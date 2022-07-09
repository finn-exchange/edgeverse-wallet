package com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV1

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
import com.edgeverse.wallet.feature_nft_impl.data.source.providers.rmrkV1.network.RmrkV1Api
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow

class RmrkV1NftProvider(
    private val chainRegistry: ChainRegistry,
    private val accountRepository: AccountRepository,
    private val api: RmrkV1Api,
    private val nftDao: NftDao
) : NftProvider {

    override suspend fun initialNftsSync(chain: Chain, metaAccount: MetaAccount, forceOverwrite: Boolean) {
        val address = metaAccount.addressIn(chain)!!
        val nfts = api.getNfts(address)

        val toSave = nfts.map {
            NftLocal(
                identifier = identifier(chain.id, it.id),
                metaId = metaAccount.id,
                chainId = chain.id,
                collectionId = it.collectionId,
                instanceId = it.instance,
                metadata = it.metadata.encodeToByteArray(),
                name = it.name,
                label = null,
                price = it.price,
                type = NftLocal.Type.RMRK1,
                issuanceMyEdition = it.edition,

                // to load at full sync
                media = null,
                issuanceTotal = null,

                wholeDetailsLoaded = false
            )
        }

        nftDao.insertNftsDiff(NftLocal.Type.RMRK1, metaAccount.id, toSave, forceOverwrite)
    }

    override suspend fun nftFullSync(nft: Nft) {
        val type = nft.type
        require(type is Nft.Type.Rmrk1)

        val collection = api.getCollection(type.collectionId)

        val metadata = nft.metadataRaw?.let {
            api.getIpfsMetadata(it.decodeToString().adoptFileStorageLinkToHttps())
        }

        nftDao.updateNft(nft.identifier) { local ->
            local.copy(
                media = metadata?.image?.adoptFileStorageLinkToHttps(),
                label = metadata?.description,
                issuanceTotal = collection.first().max,
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

            val collection = api.getCollection(nftLocal.collectionId).first()

            val metadata = collection.metadata?.let {
                api.getIpfsMetadata(it.adoptFileStorageLinkToHttps())
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
                    name = collection.name,
                    media = metadata?.image?.adoptFileStorageLinkToHttps()
                )
            )
        }
    }

    private fun identifier(chainId: ChainId, id: String): String {
        return "$chainId-$id"
    }
}
