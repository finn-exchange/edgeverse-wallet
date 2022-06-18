package com.dfinn.wallet.feature_account_impl.presentation.mnemonic.confirm

data class MnemonicWord(
    val id: Int,
    val content: String,
    val indexDisplay: String?,
    val removed: Boolean
)
