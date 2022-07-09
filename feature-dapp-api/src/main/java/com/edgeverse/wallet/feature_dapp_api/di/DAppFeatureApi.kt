package com.edgeverse.wallet.feature_dapp_api.di

import com.edgeverse.wallet.feature_dapp_api.data.repository.DAppMetadataRepository

interface DAppFeatureApi {

    val dappMetadataRepository: DAppMetadataRepository
}
