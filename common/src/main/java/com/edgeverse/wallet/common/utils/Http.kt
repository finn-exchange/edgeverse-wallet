package com.edgeverse.wallet.common.utils

fun Iterable<String>.asQueryParam() = joinToString(separator = ",")
