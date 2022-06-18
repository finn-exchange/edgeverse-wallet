package com.dfinn.wallet.feature_nft_impl.data.repository

import android.util.Log
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.core_db.dao.NftDao
import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import com.dfinn.wallet.feature_nft_api.data.model.Nft
import com.dfinn.wallet.feature_nft_api.data.model.NftDetails
import com.dfinn.wallet.feature_nft_api.data.repository.NftRepository
import com.dfinn.wallet.feature_nft_impl.data.mappers.mapNftLocalToNft
import com.dfinn.wallet.feature_nft_impl.data.mappers.mapNftTypeLocalToTypeKey
import com.dfinn.wallet.feature_nft_impl.data.source.JobOrchestrator
import com.dfinn.wallet.feature_nft_impl.data.source.NftProvidersRegistry
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val NFT_TAG = "NFT"

class NftRepositoryImpl(
    private val nftProvidersRegistry: NftProvidersRegistry,
    private val chainRegistry: ChainRegistry,
    private val jobOrchestrator: JobOrchestrator,
    private val nftDao: NftDao,
    private val exceptionHandler: HttpExceptionHandler,
) : NftRepository {

    override fun allNftFlow(metaAccount: MetaAccount): Flow<List<Nft>> {
        return nftDao.nftsFlow(metaAccount.id)
            .map { nftsLocal ->
                val chainsById = chainRegistry.chainsById.first()

                nftsLocal.mapNotNull { nftLocal ->
                    mapNftLocalToNft(chainsById, metaAccount, nftLocal)
                }
            }
    }

    override fun nftDetails(nftId: String): Flow<NftDetails> {
        return flow {
            val nftTypeKey = mapNftTypeLocalToTypeKey(nftDao.getNftType(nftId))
            val nftProvider = nftProvidersRegistry.get(nftTypeKey)

            emitAll(nftProvider.nftDetailsFlow(nftId))
        }.catch { throw exceptionHandler.transformException(it) }
    }

    override suspend fun initialNftSync(
        metaAccount: MetaAccount,
        forceOverwrite: Boolean,
    ): Unit = withContext(Dispatchers.IO) {
        val chains = chainRegistry.currentChains.first()

        val syncJobs = chains.flatMap { chain ->
            nftProvidersRegistry.get(chain).map { nftProvider ->
                // launch separate job per each nftProvider
                launch {
                    // prevent whole sync from failing if some particular provider fails
                    runCatching {
                        nftProvider.initialNftsSync(chain, metaAccount, forceOverwrite)
                    }.onFailure {
                        Log.e(NFT_TAG, "Failed to sync nfts in ${chain.name} using ${nftProvider::class.simpleName}", it)
                    }
                }
            }
        }

        syncJobs.joinAll()
    }

    override suspend fun fullNftSync(nft: Nft) = withContext(Dispatchers.IO) {
        jobOrchestrator.runUniqueJob(nft.identifier) {
            runCatching {
                nftProvidersRegistry.get(nft.type.key).nftFullSync(nft)
            }.onFailure {
                Log.e(NFT_TAG, "Failed to fully sync nft ${nft.identifier} in ${nft.chain.name} with type ${nft.type::class.simpleName}", it)
            }
        }
    }
}
