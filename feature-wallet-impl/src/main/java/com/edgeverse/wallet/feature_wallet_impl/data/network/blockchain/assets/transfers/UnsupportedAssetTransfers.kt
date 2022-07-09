package com.edgeverse.wallet.feature_wallet_impl.data.network.blockchain.assets.transfers

import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfers
import com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfersValidationSystem
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import java.math.BigInteger

class UnsupportedAssetTransfers : AssetTransfers {

    override val validationSystem: AssetTransfersValidationSystem
        get() = throw UnsupportedOperationException("Unsupported")

    override suspend fun calculateFee(transfer: AssetTransfer): BigInteger {
        throw UnsupportedOperationException("Unsupported")
    }

    override suspend fun performTransfer(transfer: AssetTransfer): Result<String> {
        return Result.failure(UnsupportedOperationException("Unsupported"))
    }

    override suspend fun areTransfersEnabled(chainAsset: Chain.Asset): Boolean {
        return false
    }
}
