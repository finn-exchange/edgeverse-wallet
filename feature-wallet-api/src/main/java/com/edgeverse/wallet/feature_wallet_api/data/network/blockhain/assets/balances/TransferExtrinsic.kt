package com.edgeverse.wallet.feature_wallet_api.data.network.blockhain.assets.balances

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.runtime.repository.ExtrinsicStatus
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import java.math.BigInteger

class TransferExtrinsic(
    val senderId: ByteArray,
    val recipientId: ByteArray,
    val amountInPlanks: BigInteger,
    val chainAsset: Chain.Asset,
    val status: ExtrinsicStatus,
    val hash: String,
)

fun List<TransferExtrinsic>.filterOwn(owner: AccountId) = filter {
    it.recipientId.contentEquals(owner) || it.senderId.contentEquals(owner)
}
