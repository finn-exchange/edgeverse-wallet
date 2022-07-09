package com.edgeverse.wallet.runtime.multiNetwork.connection.autobalance.strategy

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

interface AutoBalanceStrategy {

    fun initialNode(defaultNodes: List<Chain.Node>): Chain.Node

    fun nextNode(currentNode: Chain.Node, defaultNodes: List<Chain.Node>): Chain.Node
}
