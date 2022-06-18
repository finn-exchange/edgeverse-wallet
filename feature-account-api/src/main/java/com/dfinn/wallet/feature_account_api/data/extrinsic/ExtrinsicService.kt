package com.dfinn.wallet.feature_account_api.data.extrinsic

import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.data.secrets.v2.getAccountSecrets
import com.dfinn.wallet.common.utils.orZero
import com.dfinn.wallet.common.utils.takeWhileInclusive
import com.dfinn.wallet.common.utils.tip
import com.dfinn.wallet.feature_account_api.data.secrets.keypair
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.model.MetaAccount
import com.dfinn.wallet.feature_account_api.domain.model.cryptoTypeIn
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicBuilderFactory
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicStatus
import com.dfinn.wallet.runtime.extrinsic.create
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId
import com.dfinn.wallet.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

class ExtrinsicService(
    private val rpcCalls: RpcCalls,
    private val accountRepository: AccountRepository,
    private val secretStoreV2: SecretStoreV2,
    private val extrinsicBuilderFactory: ExtrinsicBuilderFactory,
) {

    suspend fun submitExtrinsic(
        chain: Chain,
        accountId: ByteArray,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): Result<String> = runCatching {
        val extrinsic = buildExtrinsic(chain, accountId, formExtrinsic)

        rpcCalls.submitExtrinsic(chain.id, extrinsic)
    }

    suspend fun submitAndWatchExtrinsic(
        chain: Chain,
        accountId: ByteArray,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): Flow<ExtrinsicStatus> {
        val extrinsic = buildExtrinsic(chain, accountId, formExtrinsic)

        return rpcCalls.submitAndWatchExtrinsic(chain.id, extrinsic)
            .takeWhileInclusive { !it.terminal }
    }

    private suspend fun buildExtrinsic(
        chain: Chain,
        accountId: ByteArray,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): String {
        val metaAccount = accountRepository.findMetaAccount(accountId) ?: error("No meta account found accessing ${accountId.toHexString()}")
        val keypair = secretStoreV2.getKeypairFor(metaAccount, chain, accountId)

        val extrinsicBuilder = extrinsicBuilderFactory.create(chain, keypair, metaAccount.cryptoTypeIn(chain))

        extrinsicBuilder.formExtrinsic()

        return extrinsicBuilder.build(useBatchAll = true)
    }

    suspend fun estimateFee(
        chain: Chain,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): BigInteger {
        val extrinsicBuilder = extrinsicBuilderFactory.create(chain)
        extrinsicBuilder.formExtrinsic()
        val extrinsic = extrinsicBuilder.build()

        val extrinsicType = Extrinsic.create(extrinsicBuilder.runtime)
        val decodedExtrinsic = extrinsicType.fromHex(extrinsicBuilder.runtime, extrinsic)

        val tip = decodedExtrinsic.tip().orZero()
        val baseFee = rpcCalls.getExtrinsicFee(chain.id, extrinsic)

        return tip + baseFee
    }

    suspend fun estimateFee(chainId: ChainId, extrinsic: String): BigInteger {
        return rpcCalls.getExtrinsicFee(chainId, extrinsic)
    }

    private suspend fun SecretStoreV2.getKeypairFor(
        metaAccount: MetaAccount,
        chain: Chain,
        accountId: ByteArray,
    ): Keypair {
        return getAccountSecrets(metaAccount.id, accountId).keypair(chain)
    }
}
