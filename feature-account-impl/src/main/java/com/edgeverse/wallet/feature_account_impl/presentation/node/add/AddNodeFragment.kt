package com.edgeverse.wallet.feature_account_impl.presentation.node.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.di.AccountFeatureComponent
import kotlinx.android.synthetic.main.fragment_node_add.addBtn
import kotlinx.android.synthetic.main.fragment_node_add.nodeHostField
import kotlinx.android.synthetic.main.fragment_node_add.nodeNameField
import kotlinx.android.synthetic.main.fragment_node_add.novaToolbar

class AddNodeFragment : BaseFragment<AddNodeViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_node_add, container, false)

    override fun initViews() {
        novaToolbar.setHomeButtonListener { viewModel.backClicked() }

        nodeNameField.content.bindTo(viewModel.nodeNameInputLiveData)

        nodeHostField.content.bindTo(viewModel.nodeHostInputLiveData)

        addBtn.setOnClickListener { viewModel.addNodeClicked() }

        addBtn.prepareForProgress(viewLifecycleOwner)
    }

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .addNodeComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: AddNodeViewModel) {
        viewModel.addButtonState.observe(addBtn::setState)
    }
}
