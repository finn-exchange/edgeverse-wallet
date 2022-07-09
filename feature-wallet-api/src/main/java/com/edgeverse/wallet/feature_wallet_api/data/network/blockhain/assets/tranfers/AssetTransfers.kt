package com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers

import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal
import java.math.BigInteger

data class AssetTransfer(
    val sender: MetaAccount,
    val recipient: String,
    val chain: Chain,
    val chainAsset: Chain.Asset,
    val amount: BigDecimal,
)

interface AssetTransfers {

    val validationSystem: AssetTransfersValidationSystem

    suspend fun calculateFee(transfer: AssetTransfer): BigInteger

    suspend fun performTransfer(transfer: AssetTransfer): Result<String>

    suspend fun areTransfersEnabled(chainAsset: Chain.Asset): Boolean
}
