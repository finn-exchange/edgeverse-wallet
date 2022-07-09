@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package com.edgeverse.wallet.core.model

class RuntimeConfiguration(
    val genesisHash: String,
    val erasPerDay: Int,
    val addressByte: Short,
)
