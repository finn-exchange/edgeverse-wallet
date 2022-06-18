package com.dfinn.wallet.feature_staking_impl.presentation.staking.main.unbonding

import com.dfinn.wallet.feature_staking_impl.domain.model.Unbonding
import com.dfinn.wallet.feature_wallet_api.presentation.model.AmountModel

data class UnbondingModel(
    val index: Int, // for DiffUtil to be able to distinguish unbondings with the same amount and days left
    val status: Unbonding.Status,
    val amountModel: AmountModel
)
