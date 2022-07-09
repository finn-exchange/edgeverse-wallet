package com.edgeverse.wallet.common.mixin.hints

import com.edgeverse.wallet.common.resources.ResourceManager
import kotlinx.coroutines.CoroutineScope

class ResourcesHintsMixinFactory(
    private val resourceManager: ResourceManager
) {

    fun create(coroutineScope: CoroutineScope, hintsRes: List<Int>): HintsMixin {
        return ResourcesHintsMixin(coroutineScope, resourceManager, hintsRes)
    }
}

private class ResourcesHintsMixin(
    coroutineScope: CoroutineScope,
    private val resourceManager: ResourceManager,
    private val hintsRes: List<Int>
) : ConstantHintsMixin(coroutineScope) {

    override suspend fun getHints() = hintsRes.map(resourceManager::getString)
}
