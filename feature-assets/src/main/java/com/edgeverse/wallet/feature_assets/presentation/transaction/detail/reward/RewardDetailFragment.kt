package com.edgeverse.wallet.feature_assets.presentation.transaction.detail.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.formatDateTime
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.feature_account_api.presenatation.actions.setupExternalActions
import com.edgeverse.wallet.feature_account_api.view.showAddress
import com.edgeverse.wallet.feature_account_api.view.showChain
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureComponent
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.feature_assets.presentation.model.showOperationStatus
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailAmount
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailEra
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailEvent
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailNetwork
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailStatus
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailToolbar
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailType
import kotlinx.android.synthetic.main.fragment_reward_slash_details.rewardDetailValidator

private const val KEY_REWARD = "KEY_REWARD"

class RewardDetailFragment : BaseFragment<RewardDetailViewModel>() {
    companion object {
        fun getBundle(operation: OperationParcelizeModel.Reward) = Bundle().apply {
            putParcelable(KEY_REWARD, operation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_reward_slash_details, container, false)

    override fun initViews() {
        rewardDetailToolbar.setHomeButtonListener { viewModel.backClicked() }

        rewardDetailEvent.setOnClickListener {
            viewModel.eventIdClicked()
        }

        rewardDetailValidator.setOnClickListener {
            viewModel.validatorAddressClicked()
        }
    }

    override fun inject() {
        val operation = argument<OperationParcelizeModel.Reward>(KEY_REWARD)

        FeatureUtils.getFeature<AssetsFeatureComponent>(
            requireContext(),
            AssetsFeatureApi::class.java
        )
            .rewardDetailComponentFactory()
            .create(this, operation)
            .inject(this)
    }

    override fun subscribe(viewModel: RewardDetailViewModel) {
        setupExternalActions(viewModel)

        with(viewModel.operation) {
            rewardDetailEvent.showValue(eventId)
            rewardDetailToolbar.setTitle(time.formatDateTime(requireContext()))
            rewardDetailAmount.text = amount

            rewardDetailEra.showValue(era)

            rewardDetailStatus.showOperationStatus(statusAppearance)

            rewardDetailType.showValue(type)
        }

        viewModel.validatorAddressModelFlow.observe { addressModel ->
            if (addressModel != null) {
                rewardDetailValidator.showAddress(addressModel)
            } else {
                rewardDetailValidator.makeGone()
            }
        }

        viewModel.chainUi.observe(rewardDetailNetwork::showChain)
    }
}
