package com.edgeverse.wallet.feature_staking_impl.presentation.validators.details.model

import com.edgeverse.wallet.common.address.AddressModel

class ValidatorDetailsModel(
    val stake: ValidatorStakeModel,
    val addressModel: AddressModel,
    val identity: IdentityModel?,
)
