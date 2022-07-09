package com.edgeverse.wallet.feature_crowdloan_impl.domain.contributions

import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.FundInfo
import com.edgeverse.wallet.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import com.edgeverse.wallet.feature_crowdloan_api.data.repository.ParachainMetadata
import java.math.BigInteger

class Contribution(
    val amount: BigInteger,
    val paraId: ParaId,
    val fundInfo: FundInfo,
    val sourceName: String?,
    val parachainMetadata: ParachainMetadata?,
)
