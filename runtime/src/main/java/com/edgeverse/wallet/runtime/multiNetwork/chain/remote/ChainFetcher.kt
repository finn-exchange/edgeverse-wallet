package com.edgeverse.wallet.runtime.multiNetwork.chain.remote

import com.edgeverse.wallet.runtime.BuildConfig
import com.edgeverse.wallet.runtime.multiNetwork.chain.remote.model.ChainRemote
import retrofit2.http.GET

interface ChainFetcher {

    @GET(BuildConfig.CHAINS_URL)
    suspend fun getChains(): List<ChainRemote>
}
