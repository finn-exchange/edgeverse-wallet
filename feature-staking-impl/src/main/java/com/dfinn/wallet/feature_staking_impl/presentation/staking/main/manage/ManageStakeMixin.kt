package com.dfinn.wallet.feature_staking_impl.presentation.staking.main.manage

interface ManageStakeMixin {

    val allowedStakeActions: Collection<ManageStakeAction>

    fun manageActionChosen(action: ManageStakeAction)

    interface Presentation : ManageStakeMixin
}
