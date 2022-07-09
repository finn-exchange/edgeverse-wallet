package com.edgeverse.wallet.feature_account_impl.presentation.node.mixin.impl

import androidx.lifecycle.asLiveData
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core.model.Node
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountInteractor
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.data.mappers.mapNodeToNodeModel
import com.edgeverse.wallet.feature_account_impl.presentation.node.mixin.api.NodeListingMixin
import com.edgeverse.wallet.feature_account_impl.presentation.node.model.NodeHeaderModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class NodeListingProvider(
    private val accountInteractor: AccountInteractor,
    private val resourceManager: ResourceManager,
) : NodeListingMixin {

    override val groupedNodeModelsLiveData = getGroupedNodes()
        .asLiveData()

    private fun getGroupedNodes() = accountInteractor.nodesFlow()
        .map(::transformToModels)
        .flowOn(Dispatchers.Default)

    private fun transformToModels(list: List<Node>): List<Any> {
        val defaultHeader = NodeHeaderModel(resourceManager.getString(R.string.connection_management_default_title))
        val customHeader = NodeHeaderModel(resourceManager.getString(R.string.connection_management_custom_title))

        val defaultNodes = list.filter(Node::isDefault)
        val customNodes = list.filter { !it.isDefault }

        return mutableListOf<Any>().apply {
            add(defaultHeader)
            addAll(defaultNodes.map(::mapNodeToNodeModel))

            if (customNodes.isNotEmpty()) {
                add(customHeader)
                addAll(customNodes.map(::mapNodeToNodeModel))
            }
        }
    }
}
