package com.dfinn.wallet.feature_account_api.domain.model

import com.dfinn.wallet.runtime.multiNetwork.chain.model.ChainId

sealed class AddAccountType {

    class MetaAccount(val name: String) : AddAccountType()

    class ChainAccount(val chainId: ChainId, val metaId: Long) : AddAccountType()
}
