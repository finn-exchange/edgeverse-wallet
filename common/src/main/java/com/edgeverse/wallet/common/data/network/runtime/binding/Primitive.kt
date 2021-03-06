package com.edgeverse.wallet.common.data.network.runtime.binding

import java.math.BigInteger

@HelperBinding
fun bindNumber(dynamicInstance: Any?): BigInteger = dynamicInstance.cast()

@HelperBinding
fun bindString(dynamicInstance: Any?): String = dynamicInstance.cast<ByteArray>().decodeToString()

@HelperBinding
fun bindBoolean(dynamicInstance: Any?): Boolean = dynamicInstance.cast()
