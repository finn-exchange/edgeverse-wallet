package com.edgeverse.wallet.feature_account_impl.presentation.node.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.onTextChanged
import com.edgeverse.wallet.common.utils.setDrawableStart
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.di.AccountFeatureComponent
import kotlinx.android.synthetic.main.fragment_node_details.nodeDetailsHostField
import kotlinx.android.synthetic.main.fragment_node_details.nodeDetailsNameField
import kotlinx.android.synthetic.main.fragment_node_details.nodeDetailsNetworkType
import kotlinx.android.synthetic.main.fragment_node_details.nodeHostCopy
import kotlinx.android.synthetic.main.fragment_node_details.novaToolbar
import kotlinx.android.synthetic.main.fragment_node_details.updateBtn

class NodeDetailsFragment : BaseFragment<NodeDetailsViewModel>() {

    companion object {
        private const val KEY_NODE_ID = "node_id"

        fun getBundle(nodeId: Int): Bundle {
            return Bundle().apply {
                putInt(KEY_NODE_ID, nodeId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_node_details, container, false)

    override fun initViews() {
        novaToolbar.setHomeButtonListener { viewModel.backClicked() }

        nodeHostCopy.setOnClickListener {
            viewModel.copyNodeHostClicked()
        }

        updateBtn.setOnClickListener {
            viewModel.updateClicked(nodeDetailsNameField.content.text.toString(), nodeDetailsHostField.content.text.toString())
        }
    }

    override fun inject() {
        val nodeId = argument<Int>(KEY_NODE_ID)
        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .nodeDetailsComponentFactory()
            .create(this, nodeId)
            .inject(this)
    }

    override fun subscribe(viewModel: NodeDetailsViewModel) {
        viewModel.nodeModelLiveData.observe { node ->
            nodeDetailsNameField.content.setText(node.name)
            nodeDetailsHostField.content.setText(node.link)

            with(node.networkModelType) {
                nodeDetailsNetworkType.text = networkType.readableName
                nodeDetailsNetworkType.setDrawableStart(icon)
            }
        }

        viewModel.nameEditEnabled.observe { editEnabled ->
            updateBtn.setVisible(editEnabled)

            nodeDetailsNameField.content.isEnabled = editEnabled

            nodeDetailsNameField.content.onTextChanged {
                viewModel.nodeDetailsEdited()
            }
        }

        viewModel.hostEditEnabled.observe { editEnabled ->
            nodeDetailsHostField.content.isEnabled = editEnabled

            nodeDetailsHostField.content.onTextChanged {
                viewModel.nodeDetailsEdited()
            }
        }

        viewModel.updateButtonEnabled.observe {
            updateBtn.isEnabled = it
        }
    }
}
