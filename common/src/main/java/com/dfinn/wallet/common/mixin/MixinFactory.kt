package com.dfinn.wallet.common.mixin

import kotlinx.coroutines.CoroutineScope

interface MixinFactory<M> {

    fun create(scope: CoroutineScope): M
}
