package com.edgeverse.wallet.common.mixin.hints

import com.edgeverse.wallet.common.utils.WithCoroutineScopeExtensions
import com.edgeverse.wallet.common.utils.flowOf
import com.edgeverse.wallet.common.utils.inBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class ConstantHintsMixin(
    coroutineScope: CoroutineScope
) : HintsMixin,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    abstract suspend fun getHints(): List<String>

    override val hintsFlow: Flow<List<String>> = flowOf {
        getHints()
    }
        .inBackground()
        .shareLazily()
}
