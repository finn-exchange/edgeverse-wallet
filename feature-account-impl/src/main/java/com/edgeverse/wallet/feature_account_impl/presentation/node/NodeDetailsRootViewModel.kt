package com.edgeverse.wallet.feature_account_impl.presentation.node

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.base.errors.NovaException
import com.edgeverse.wallet.common.resources.ResourceManager
import com.edgeverse.wallet.core.model.Node
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.domain.errors.NodeAlreadyExistsException
import com.edgeverse.wallet.feature_account_impl.domain.errors.UnsupportedNetworkException

abstract class NodeDetailsRootViewModel(
    private val resourceManager: ResourceManager
) : BaseViewModel() {

    protected open fun handleNodeException(throwable: Throwable) {
        when (throwable) {
            is NodeAlreadyExistsException -> showError(resourceManager.getString(R.string.connection_add_already_exists_error))
            is UnsupportedNetworkException -> showError(getUnsupportedNodeError())
            is NovaException -> {
                if (NovaException.Kind.NETWORK == throwable.kind) {
                    showError(resourceManager.getString(R.string.connection_add_invalid_error))
                } else {
                    throwable.message?.let(::showError)
                }
            }
            else -> throwable.message?.let(::showError)
        }
    }

    protected open fun getUnsupportedNodeError(): String {
        val supportedNodes = Node.NetworkType.values().joinToString(", ") { it.readableName }
        val unsupportedNodeErrorMsg = resourceManager.getString(R.string.connection_add_unsupported_error)
        return unsupportedNodeErrorMsg.format(supportedNodes)
    }
}
