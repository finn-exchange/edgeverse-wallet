package com.dfinn.wallet.feature_staking_impl.data.network.blockhain.updaters.base

import com.dfinn.wallet.common.utils.Modules
import com.dfinn.wallet.core.updater.Updater

interface StakingUpdater : Updater {

    override val requiredModules: List<String>
        get() = listOf(Modules.STAKING)
}
