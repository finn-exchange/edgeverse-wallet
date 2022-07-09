package com.edgeverse.wallet.core.model

data class Network(
    val type: Node.NetworkType
) {
    val name = type.readableName
}
