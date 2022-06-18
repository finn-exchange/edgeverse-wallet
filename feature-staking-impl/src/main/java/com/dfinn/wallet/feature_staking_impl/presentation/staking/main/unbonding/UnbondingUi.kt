package com.dfinn.wallet.feature_staking_impl.presentation.staking.main.unbonding

import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.feature_staking_impl.presentation.staking.main.unbonding.rebond.ChooseRebondKindBottomSheet

fun BaseFragment<*>.setupUnbondingMixin(mixin: UnbondingMixin, view: UnbondingsView) {
    mixin.rebondKindAwaitable.awaitableActionLiveData.observeEvent {
        ChooseRebondKindBottomSheet(requireContext(), it.onSuccess, it.onCancel)
            .show()
    }

    view.onCancelClicked { mixin.cancelClicked() }
    view.onRedeemClicked { mixin.redeemClicked() }

    mixin.state.observe(view::setState)
}
