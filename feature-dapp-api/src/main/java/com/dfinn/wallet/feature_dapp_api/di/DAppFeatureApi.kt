package com.dfinn.wallet.feature_dapp_api.di

import com.dfinn.wallet.feature_dapp_api.data.repository.DAppMetadataRepository

interface DAppFeatureApi {

    val dappMetadataRepository: DAppMetadataRepository
}
