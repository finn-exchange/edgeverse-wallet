package com.edgeverse.wallet.feature_account_impl.data.repository.datasource

import com.edgeverse.wallet.common.data.secrets.v1.SecretStoreV1
import com.edgeverse.wallet.common.data.secrets.v2.ChainAccountSecrets
import com.edgeverse.wallet.common.data.secrets.v2.MetaAccountSecrets
import com.edgeverse.wallet.core.model.CryptoType
import com.edgeverse.wallet.core.model.Language
import com.edgeverse.wallet.core.model.Node
import com.edgeverse.wallet.feature_account_api.domain.model.Account
import com.edgeverse.wallet.feature_account_api.domain.model.AuthType
import com.edgeverse.wallet.feature_account_api.domain.model.LightMetaAccount
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccount
import com.edgeverse.wallet.feature_account_api.domain.model.MetaAccountOrdering
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import kotlinx.coroutines.flow.Flow

interface AccountDataSource : SecretStoreV1 {

    suspend fun saveAuthType(authType: AuthType)

    suspend fun getAuthType(): AuthType

    suspend fun savePinCode(pinCode: String)

    suspend fun getPinCode(): String?

    suspend fun saveSelectedNode(node: Node)

    suspend fun getSelectedNode(): Node?

    suspend fun anyAccountSelected(): Boolean

    suspend fun saveSelectedAccount(account: Account)

    // TODO for compatibility only
    val selectedAccountMapping: Flow<Map<ChainId, Account?>>

    suspend fun getSelectedMetaAccount(): MetaAccount
    fun selectedMetaAccountFlow(): Flow<MetaAccount>
    suspend fun findMetaAccount(accountId: ByteArray): MetaAccount?

    suspend fun allMetaAccounts(): List<MetaAccount>

    fun lightMetaAccountsFlow(): Flow<List<LightMetaAccount>>
    suspend fun selectMetaAccount(metaId: Long)
    suspend fun updateAccountPositions(accountOrdering: List<MetaAccountOrdering>)

    fun selectedNodeFlow(): Flow<Node>

    suspend fun getSelectedLanguage(): Language
    suspend fun changeSelectedLanguage(language: Language)

    suspend fun accountExists(accountId: AccountId): Boolean
    suspend fun getMetaAccount(metaId: Long): MetaAccount

    suspend fun updateMetaAccountName(metaId: Long, newName: String)
    suspend fun deleteMetaAccount(metaId: Long)

    /**
     * @return id of inserted meta account
     */
    suspend fun insertMetaAccount(
        name: String,
        substrateCryptoType: CryptoType,
        secrets: EncodableStruct<MetaAccountSecrets>
    ): Long

    /**
     * @return id of inserted meta account
     */
    suspend fun insertChainAccount(
        metaId: Long,
        chain: Chain,
        cryptoType: CryptoType,
        secrets: EncodableStruct<ChainAccountSecrets>
    )
}
