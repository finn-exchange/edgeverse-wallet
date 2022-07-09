package com.edgeverse.wallet.common.utils

class IgnoredOnEquals<out T>(val value: T) {

    override fun equals(other: Any?): Boolean {
        return true
    }
}
