package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.base

import com.edgeverse.wallet.common.utils.Modules
import com.edgeverse.wallet.core.updater.Updater

interface StakingUpdater : Updater {

    override val requiredModules: List<String>
        get() = listOf(Modules.STAKING)
}
