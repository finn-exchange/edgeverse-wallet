package com.edgeverse.wallet.feature_staking_impl.presentation.confirm.nominations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.feature_staking_api.di.StakingFeatureApi
import com.edgeverse.wallet.feature_staking_impl.R
import com.edgeverse.wallet.feature_staking_impl.di.StakingFeatureComponent
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.ValidatorsAdapter
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.change.ValidatorModel
import kotlinx.android.synthetic.main.fragment_confirm_nominations.confirmNominationsList
import kotlinx.android.synthetic.main.fragment_confirm_nominations.confirmNominationsToolbar

class ConfirmNominationsFragment : BaseFragment<ConfirmNominationsViewModel>(), ValidatorsAdapter.ItemHandler {

    lateinit var adapter: ValidatorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_nominations, container, false)
    }

    override fun initViews() {
        adapter = ValidatorsAdapter(this)
        confirmNominationsList.adapter = adapter

        confirmNominationsList.setHasFixedSize(true)

        confirmNominationsToolbar.setHomeButtonListener {
            viewModel.backClicked()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .confirmNominationsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmNominationsViewModel) {
        viewModel.selectedValidatorsLiveData.observe(adapter::submitList)

        viewModel.toolbarTitle.observe(confirmNominationsToolbar::setTitle)
    }

    override fun validatorInfoClicked(validatorModel: ValidatorModel) {
        viewModel.validatorInfoClicked(validatorModel)
    }

    override fun validatorClicked(validatorModel: ValidatorModel) {
        viewModel.validatorInfoClicked(validatorModel)
    }
}
