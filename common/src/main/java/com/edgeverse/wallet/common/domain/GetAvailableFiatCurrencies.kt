package com.edgeverse.wallet.common.domain

import com.edgeverse.wallet.common.data.network.coingecko.CoingeckoApi
import com.edgeverse.wallet.common.data.network.coingecko.FiatCurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

typealias FiatCurrencies = List<FiatCurrency>

class GetAvailableFiatCurrencies(private val coingeckoApi: CoingeckoApi) {

    private var cache = MutableStateFlow<FiatCurrencies>(listOf())
    private var syncTimeMillis = 0L

    @OptIn(ExperimentalTime::class)
    private val minRatesRefreshDuration = 2.toDuration(DurationUnit.HOURS)

    operator fun get(index: String): FiatCurrency? = cache.value.firstOrNull { it.id == index }

    suspend operator fun invoke(): FiatCurrencies {
        sync()
        return cache.value
    }

    fun flow(): Flow<FiatCurrencies> = cache

    @OptIn(ExperimentalTime::class)
    suspend fun sync() {
        val shouldRefreshRates = Calendar.getInstance().timeInMillis - syncTimeMillis > minRatesRefreshDuration.toInt(DurationUnit.MILLISECONDS)
        if (shouldRefreshRates) {
            runCatching {
                val supportedCurrencies = coingeckoApi.getSupportedCurrencies()
                val config = coingeckoApi.getFiatConfig()
                cache.value = config.filter { it.id in supportedCurrencies }
                syncTimeMillis = Calendar.getInstance().timeInMillis
            }
        }
    }
}

operator fun FiatCurrencies.get(index: String) = firstOrNull { it.id == index }
