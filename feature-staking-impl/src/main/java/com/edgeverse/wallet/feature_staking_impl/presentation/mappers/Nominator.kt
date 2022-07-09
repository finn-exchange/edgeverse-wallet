package com.edgeverse.wallet.feature_staking_impl.presentation.mappers

import com.edgeverse.wallet.feature_staking_api.domain.model.IndividualExposure
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.parcel.NominatorParcelModel

fun mapNominatorToNominatorParcelModel(nominator: IndividualExposure): NominatorParcelModel {
    return with(nominator) {
        NominatorParcelModel(who, value)
    }
}
