package com.edgeverse.wallet.feature_staking_impl.data.mappers

import com.edgeverse.wallet.feature_account_api.data.mappers.stubNetwork
import com.edgeverse.wallet.feature_account_api.domain.model.Account
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_account_api.domain.model.addressIn
import com.edgeverse.wallet.feature_staking_api.domain.model.StakingAccount
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain

fun mapAccountToStakingAccount(account: Account) = with(account) {
    StakingAccount(address, name, network)
}

fun mapAccountToStakingAccount(chain: Chain, metaAccount: MetaAccount) = with(metaAccount) {
    StakingAccount(
        address = addressIn(chain)!!, // TODO may be null in ethereum
        name = name,
        network = stubNetwork(chain.id),
    )
}
