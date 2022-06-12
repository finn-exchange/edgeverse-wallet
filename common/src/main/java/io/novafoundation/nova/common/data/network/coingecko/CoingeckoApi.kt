package io.novafoundation.nova.common.data.network.coingecko

import retrofit2.http.GET
import retrofit2.http.Query

interface CoingeckoApi {

    @GET("//api.coingecko.com/api/v3/simple/price")
    suspend fun getAssetPrice(
        @Query("ids") priceIds: String,
        @Query("vs_currencies") currency: String,
        @Query("include_24hr_change") includeRateChange: Boolean
    ): Map<String, PriceInfo>

    @GET("//api.coingecko.com/api/v3/simple/supported_vs_currencies")
    suspend fun getSupportedCurrencies(): List<String>

    @GET("https://raw.githubusercontent.com/soramitsu/fearless-utils/android/v2/fiat/fiats.json")
    suspend fun getFiatConfig(): List<FiatCurrency>

}
