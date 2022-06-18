package com.dfinn.wallet.common.utils

fun Iterable<String>.asQueryParam() = joinToString(separator = ",")
