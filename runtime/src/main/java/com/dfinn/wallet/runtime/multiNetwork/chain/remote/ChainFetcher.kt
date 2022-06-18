package com.dfinn.wallet.runtime.multiNetwork.chain.remote

import com.dfinn.wallet.runtime.BuildConfig
import com.dfinn.wallet.runtime.multiNetwork.chain.remote.model.ChainRemote
import retrofit2.http.GET

interface ChainFetcher {

    @GET(BuildConfig.CHAINS_URL)
    suspend fun getChains(): List<ChainRemote>
}
