package com.dfinn.wallet.core.model

data class Network(
    val type: Node.NetworkType
) {
    val name = type.readableName
}
