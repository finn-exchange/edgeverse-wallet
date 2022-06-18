package com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets

import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.balances.AssetBalance
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.history.AssetHistory
import com.dfinn.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfers

interface AssetSource {

    val transfers: AssetTransfers

    val balance: AssetBalance

    val history: AssetHistory
}
