package com.dfinn.wallet.feature_staking_impl.presentation.validators.details.model

import com.dfinn.wallet.common.address.AddressModel

class ValidatorDetailsModel(
    val stake: ValidatorStakeModel,
    val addressModel: AddressModel,
    val identity: IdentityModel?,
)
