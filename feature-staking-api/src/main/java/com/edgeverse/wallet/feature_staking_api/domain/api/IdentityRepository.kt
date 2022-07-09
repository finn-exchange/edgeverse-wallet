package com.edgeverse.wallet.feature_staking_api.domain.api

import com.edgeverse.wallet.feature_staking_api.domain.model.Identity
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId

interface IdentityRepository {

    suspend fun getIdentitiesFromIds(chainId: ChainId, accountIdsHex: List<String>): AccountIdMap<Identity?>

    suspend fun getIdentitiesFromAddresses(chain: Chain, accountAddresses: List<String>): AccountAddressMap<Identity?>
}
