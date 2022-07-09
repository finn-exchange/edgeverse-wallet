package com.edgeverse.wallet.feature_dapp_impl.data.repository

import com.edgeverse.wallet.common.utils.retryUntilDone
import com.edgeverse.wallet.core_db.dao.PhishingSitesDao
import com.edgeverse.wallet.core_db.model.PhishingSiteLocal
import com.edgeverse.wallet.feature_dapp_impl.data.network.phishing.PhishingSitesApi
import com.edgeverse.wallet.feature_dapp_impl.util.Urls

interface PhishingSitesRepository {

    suspend fun syncPhishingSites()

    suspend fun isPhishing(url: String): Boolean
}

class PhishingSitesRepositoryImpl(
    private val phishingSitesDao: PhishingSitesDao,
    private val phishingSitesApi: PhishingSitesApi,
) : PhishingSitesRepository {

    override suspend fun syncPhishingSites() {
        val remotePhishingSites = retryUntilDone { phishingSitesApi.getPhishingSites() }
        val toInsert = remotePhishingSites.deny.map(::PhishingSiteLocal)

        phishingSitesDao.updatePhishingSites(toInsert)
    }

    override suspend fun isPhishing(url: String): Boolean {
        val host = Urls.hostOf(url)
        val hostSuffixes = extractAllPossibleSubDomains(host)

        return phishingSitesDao.isPhishing(hostSuffixes)
    }

    private fun extractAllPossibleSubDomains(host: String): List<String> {
        val separator = "."

        val segments = host.split(separator)

        val suffixes = (2..segments.size).map { suffixLength ->
            segments.takeLast(suffixLength).joinToString(separator = ".")
        }

        return suffixes
    }
}
