package com.dfinn.wallet.feature_nft_impl.presentation.nft.details

import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.feature_account_api.presenatation.chain.ChainUi
import com.dfinn.wallet.feature_wallet_api.presentation.model.AmountModel

class NftDetailsModel(
    val media: String?,
    val name: String,
    val issuance: String,
    val description: String?,
    val price: AmountModel?,
    val collection: Collection?,
    val owner: AddressModel,
    val creator: AddressModel?,
    val network: ChainUi
) {

    class Collection(
        val name: String,
        val media: String?
    )
}
