package com.edgeverse.wallet.runtime.extrinsic

import com.edgeverse.wallet.common.data.mappers.mapCryptoTypeToEncryption
import com.edgeverse.wallet.common.utils.orZero
import com.edgeverse.wallet.core.model.CryptoType
import com.edgeverse.wallet.runtime.ext.accountIdOf
import com.edgeverse.wallet.runtime.ext.addressOf
import com.edgeverse.wallet.runtime.ext.genesisHash
import com.edgeverse.wallet.runtime.multiNetwork.ChainRegistry
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.getRuntime
import com.edgeverse.wallet.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.encrypt.MultiChainEncryption
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.ethereum.EthereumKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.keypair.substrate.SubstrateKeypairFactory
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.instances.AddressInstanceConstructor
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val FAKE_CRYPTO_TYPE = CryptoType.ECDSA

class ExtrinsicBuilderFactory(
    private val rpcCalls: RpcCalls,
    private val chainRegistry: ChainRegistry,
    private val mortalityConstructor: MortalityConstructor,
) {

    /**
     * Create with fake keypair
     * Should be primarily used for fee calculation
     */
    suspend fun create(
        chain: Chain,
    ) = create(chain, generateFakeKeyPair(chain), FAKE_CRYPTO_TYPE)

    /**
     * Create with real keypair
     */
    suspend fun create(
        chain: Chain,
        keypair: Keypair,
        cryptoType: CryptoType,
    ): ExtrinsicBuilder {
        val multiChainEncryption = if (chain.isEthereumBased) {
            MultiChainEncryption.Ethereum
        } else {
            val encryptionType = mapCryptoTypeToEncryption(cryptoType)

            MultiChainEncryption.Substrate(encryptionType)
        }

        val runtime = chainRegistry.getRuntime(chain.id)

        val accountId = chain.accountIdOf(publicKey = keypair.publicKey)
        val accountAddress = chain.addressOf(accountId)

        val nonce = rpcCalls.getNonce(chain.id, accountAddress)
        val runtimeVersion = rpcCalls.getRuntimeVersion(chain.id)
        val mortality = mortalityConstructor.constructMortality(chain.id)

        return ExtrinsicBuilder(
            tip = chain.additional?.defaultTip.orZero(),
            runtime = runtime,
            keypair = keypair,
            nonce = nonce,
            runtimeVersion = runtimeVersion,
            genesisHash = chain.genesisHash.fromHex(),
            blockHash = mortality.blockHash.fromHex(),
            era = mortality.era,
            multiChainEncryption = multiChainEncryption,
            customSignedExtensions = CustomSignedExtensions.extensionsWithValues(runtime),
            accountIdentifier = AddressInstanceConstructor.constructInstance(runtime.typeRegistry, accountId)
        )
    }

    private suspend fun generateFakeKeyPair(chain: Chain) = withContext(Dispatchers.Default) {
        if (chain.isEthereumBased) {
            val emptySeed = ByteArray(64) { 1 }

            EthereumKeypairFactory.generate(emptySeed, junctions = emptyList())
        } else {
            val cryptoType = mapCryptoTypeToEncryption(FAKE_CRYPTO_TYPE)
            val emptySeed = ByteArray(32) { 1 }

            SubstrateKeypairFactory.generate(cryptoType, emptySeed, junctions = emptyList())
        }
    }
}
