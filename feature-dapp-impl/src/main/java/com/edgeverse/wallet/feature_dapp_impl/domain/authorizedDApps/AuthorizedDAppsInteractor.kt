package com.edgeverse.wallet.feature_dapp_impl.domain.authorizedDApps

import com.edgeverse.wallet.common.utils.mapList
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_dapp_api.data.model.DappMetadata
import com.edgeverse.wallet.feature_dapp_api.data.repository.DAppMetadataRepository
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session
import com.edgeverse.wallet.feature_dapp_impl.web3.session.Web3Session.Authorization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class AuthorizedDAppsInteractor(
    private val accountRepository: AccountRepository,
    private val metadataRepository: DAppMetadataRepository,
    private val web3Session: Web3Session
) {

    suspend fun revokeAuthorization(url: String) {
        val currentAccount = accountRepository.getSelectedMetaAccount()

        web3Session.revokeAuthorization(url, currentAccount.id)
    }

    fun observeAuthorizedDApps(): Flow<List<AuthorizedDApp>> {
        return accountRepository.selectedMetaAccountFlow().flatMapLatest { metaAccount ->
            val dAppMetadatas = metadataRepository.getDAppMetadatas().associateBy(DappMetadata::baseUrl)

            web3Session.observeAuthorizationsFor(metaAccount.id)
                .map { authorizations -> authorizations.filter { it.state == Authorization.State.ALLOWED } }
                .mapList { authorization ->
                    val metadata = dAppMetadatas[authorization.baseUrl]

                    AuthorizedDApp(
                        baseUrl = authorization.baseUrl,
                        name = metadata?.name ?: authorization.dAppTitle,
                        iconLink = metadata?.iconLink
                    )
                }
        }
    }
}
