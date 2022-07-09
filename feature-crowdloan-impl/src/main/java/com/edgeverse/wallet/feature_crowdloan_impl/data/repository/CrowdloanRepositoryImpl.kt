package com.edgeverse.wallet.feature_crowdloan_impl.data.repository

import com.edgeverse.wallet.common.utils.Modules
import com.edgeverse.wallet.common.utils.crowdloan
import com.edgeverse.wallet.common.utils.hasModule
import com.edgeverse.wallet.common.utils.numberConstant
import com.edgeverse.wallet.common.utils.slots
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.DirectContribution
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.FundInfo
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.LeaseEntry
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.bindContribution
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.bindFundInfo
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.bindLeases
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.CrowdloanRepository
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.ParachainMetadata
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parachain.ParachainMetadataApi
import com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.parachain.mapParachainMetadataRemoteToParachainMetadata
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import com.edgeverse.wallet.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.hash.Hasher.blake2b256
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.primitives.u32
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.toByteArray
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.math.BigInteger

private const val CONTRIBUTIONS_CHILD_SUFFIX = "crowdloan"

class CrowdloanRepositoryImpl(
    private val remoteStorage: StorageDataSource,
    private val chainRegistry: ChainRegistry,
    private val parachainMetadataApi: ParachainMetadataApi
) : CrowdloanRepository {

    override suspend fun isCrowdloansAvailable(chainId: ChainId): Boolean {
        return runtimeFor(chainId).metadata.hasModule(Modules.CROWDLOAN)
    }

    override suspend fun allFundInfos(chainId: ChainId): Map<ParaId, FundInfo> {
        return remoteStorage.query(chainId) {
            runtime.metadata.crowdloan().storage("Funds").entries(
                keyExtractor = { (paraId: BigInteger) -> paraId },
                binding = { scale, paraId -> bindFundInfo(scale!!, runtime, paraId) }
            )
        }
    }

    override suspend fun getWinnerInfo(chainId: ChainId, funds: Map<ParaId, FundInfo>): Map<ParaId, Boolean> {
        return remoteStorage.query(chainId) {
            runtime.metadata.slots().storage("Leases").singleArgumentEntries(
                keysArguments = funds.keys,
                binding = { scale, paraId ->
                    val leases = scale?.let { bindLeases(it, runtime) }
                    val fund = funds.getValue(paraId)

                    leases?.let { isWinner(leases, fund) } ?: false
                }
            )
        }
    }

    private fun isWinner(leases: List<LeaseEntry?>, fundInfo: FundInfo): Boolean {
        return leases.any { it.isOwnedBy(fundInfo.bidderAccountId) || it.isOwnedBy(fundInfo.pre9180BidderAccountId) }
    }

    private fun LeaseEntry?.isOwnedBy(accountId: AccountId): Boolean = this?.accountId.contentEquals(accountId)

    override suspend fun getParachainMetadata(chain: Chain): Map<ParaId, ParachainMetadata> {
        return withContext(Dispatchers.Default) {
            chain.externalApi?.crowdloans?.let { section ->
                parachainMetadataApi.getParachainMetadata(section.url)
                    .associateBy { it.paraid }
                    .mapValues { (_, remoteMetadata) -> mapParachainMetadataRemoteToParachainMetadata(remoteMetadata) }
            } ?: emptyMap()
        }
    }

    override suspend fun blocksPerLeasePeriod(chainId: ChainId): BigInteger {
        val runtime = runtimeFor(chainId)

        return runtime.metadata.slots().numberConstant("LeasePeriod", runtime)
    }

    override fun fundInfoFlow(chainId: ChainId, parachainId: ParaId): Flow<FundInfo> {
        return remoteStorage.observe(
            keyBuilder = { it.metadata.crowdloan().storage("Funds").storageKey(it, parachainId) },
            binder = { scale, runtime -> bindFundInfo(scale!!, runtime, parachainId) },
            chainId = chainId
        )
    }

    override suspend fun getFundInfo(chainId: ChainId, parachainId: ParaId): FundInfo {
        return remoteStorage.query(
            keyBuilder = { it.metadata.crowdloan().storage("Funds").storageKey(it, parachainId) },
            binding = { scale, runtime -> bindFundInfo(scale!!, runtime, parachainId) },
            chainId = chainId
        )
    }

    override suspend fun minContribution(chainId: ChainId): BigInteger {
        val runtime = runtimeFor(chainId)

        return runtime.metadata.crowdloan().numberConstant("MinContribution", runtime)
    }

    override suspend fun getContribution(
        chainId: ChainId,
        accountId: AccountId,
        paraId: ParaId,
        trieIndex: BigInteger,
    ): DirectContribution? {
        return remoteStorage.queryChildState(
            storageKeyBuilder = { accountId.toHexString(withPrefix = true) },
            childKeyBuilder = {
                val suffix = (CONTRIBUTIONS_CHILD_SUFFIX.encodeToByteArray() + u32.toByteArray(it, trieIndex))
                    .blake2b256()

                write(suffix)
            },
            binder = { scale, runtime -> scale?.let { bindContribution(it, runtime) } },
            chainId = chainId
        )
    }

    private suspend fun runtimeFor(chainId: String) = chainRegistry.getRuntime(chainId)
}
