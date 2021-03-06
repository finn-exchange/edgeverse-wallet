package com.edgeverse.wallet.core_db.dao

import com.dfinn.wallet.common.utils.CollectionDiffer
import com.dfinn.wallet.core.model.CryptoType
import com.dfinn.wallet.core_db.model.chain.*

fun createTestChain(
    id: String,
    name: String = id,
    nodesCount: Int = 3,
    assetsCount: Int = 2,
): JoinedChainInfo {
    val chain = chainOf(id, name)
    val nodes = with(chain) {
        (1..nodesCount).map {
            nodeOf("link${it}")
        }
    }
    val assets = with(chain) {
        (1..assetsCount).map {
            assetOf(assetId = it, symbol = it.toString())
        }
    }

    return JoinedChainInfo(chain, nodes, assets, explorers = emptyList())
}

fun chainOf(
    id: String,
    name: String = id,
) = ChainLocal(
    id = id,
    parentId = null,
    name = name,
    icon = "Test",
    types = null,
    prefix = 0,
    isTestNet = false,
    isEthereumBased = false,
    externalApi = null,
    hasCrowdloans = false,
    color = "test",
    additional = ""
)

fun ChainLocal.nodeOf(
    link: String,
) = ChainNodeLocal(
    name = "Test",
    url = link,
    chainId = id
)

fun ChainLocal.assetOf(
    assetId: Int,
    symbol: String,
) = ChainAssetLocal(
    name = "Test",
    chainId = id,
    symbol = symbol,
    id = assetId,
    precision = 10,
    priceId = null,
    staking = "test",
    icon = "test",
    type = "test",
    buyProviders = "test",
    typeExtras = null
)

suspend fun ChainDao.addChains(chains: List<JoinedChainInfo>) {
    applyDiff(
        chainDiff = addedDiff(chains.map(JoinedChainInfo::chain)),
        assetsDiff = addedDiff(chains.flatMap(JoinedChainInfo::assets)),
        nodesDiff = addedDiff(chains.flatMap(JoinedChainInfo::nodes)),
        explorersDiff = addedDiff(chains.flatMap(JoinedChainInfo::explorers))
    )
}

suspend fun ChainDao.addChain(joinedChainInfo: JoinedChainInfo) = addChains(listOf(joinedChainInfo))

suspend fun ChainDao.removeChain(joinedChainInfo: JoinedChainInfo) {
    applyDiff(
        chainDiff = removedDiff(joinedChainInfo.chain),
        assetsDiff = removedDiff(joinedChainInfo.assets),
        nodesDiff = removedDiff(joinedChainInfo.nodes),
        explorersDiff = removedDiff(joinedChainInfo.explorers)
    )
}

suspend fun ChainDao.updateChain(joinedChainInfo: JoinedChainInfo) {
    applyDiff(
        chainDiff = updatedDiff(joinedChainInfo.chain),
        assetsDiff = updatedDiff(joinedChainInfo.assets),
        nodesDiff = updatedDiff(joinedChainInfo.nodes),
        explorersDiff = updatedDiff(joinedChainInfo.explorers)
    )
}

fun <T> addedDiff(elements: List<T>) = CollectionDiffer.Diff(
    added = elements,
    updated = emptyList(),
    removed = emptyList()
)

fun <T> updatedDiff(elements: List<T>) = CollectionDiffer.Diff(
    added = emptyList(),
    updated = elements,
    removed = emptyList()
)

fun <T> updatedDiff(element: T) = updatedDiff(listOf(element))

fun <T> addedDiff(element: T) = addedDiff(listOf(element))

fun <T> removedDiff(element: T) = removedDiff(listOf(element))

fun <T> removedDiff(elements: List<T>) = CollectionDiffer.Diff(
    added = emptyList(),
    updated = emptyList(),
    removed = elements
)

fun <T> emptyDiff() = CollectionDiffer.Diff<T>(emptyList(), emptyList(), emptyList())

fun testMetaAccount(name: String = "Test") = MetaAccountLocal(
    substratePublicKey = byteArrayOf(),
    substrateCryptoType = CryptoType.SR25519,
    ethereumPublicKey = null,
    name = name,
    isSelected = false,
    substrateAccountId = byteArrayOf(),
    ethereumAddress = null,
    position = 0
)

fun testChainAccount(
    metaId: Long,
    chainId: String,
    accountId: ByteArray = byteArrayOf()
) = ChainAccountLocal(
    metaId = metaId,
    chainId = chainId,
    publicKey = byteArrayOf(),
    cryptoType = CryptoType.SR25519,
    accountId = accountId
)
