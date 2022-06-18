package com.dfinn.wallet.feature_staking_api.domain.api

import com.dfinn.wallet.feature_staking_api.domain.model.Identity
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId

interface IdentityRepository {

    suspend fun getIdentitiesFromIds(chainId: ChainId, accountIdsHex: List<String>): AccountIdMap<Identity?>

    suspend fun getIdentitiesFromAddresses(chain: Chain, accountAddresses: List<String>): AccountAddressMap<Identity?>
}
