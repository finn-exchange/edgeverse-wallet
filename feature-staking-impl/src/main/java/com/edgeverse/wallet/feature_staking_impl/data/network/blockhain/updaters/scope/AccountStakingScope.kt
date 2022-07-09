package com.edgeverse.wallet.feature_staking_impl.data.network.blockhain.updaters.scope

import com.edgeverse.wallet.common.utils.combineToPair
import com.edgeverse.wallet.core.updater.UpdateScope
import com.edgeverse.wallet.core_db.dao.AccountStakingDao
import com.edgeverse.wallet.core_db.model.AccountStakingLocal
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.model.accountIdIn
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.runtime.state.chainAndAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class AccountStakingScope(
    private val accountRepository: AccountRepository,
    private val accountStakingDao: AccountStakingDao,
    private val sharedStakingState: StakingSharedState
) : UpdateScope {

    override fun invalidationFlow(): Flow<Any> {
        return combineToPair(
            sharedStakingState.assetWithChain,
            accountRepository.selectedMetaAccountFlow()
        ).flatMapLatest { (chainWithAsset, account) ->
            val (chain, chainAsset) = chainWithAsset

            accountStakingDao.observeDistinct(chain.id, chainAsset.id, account.accountIdIn(chain)!!)
        }
    }

    suspend fun getAccountStaking(): AccountStakingLocal {
        val (chain, chainAsset) = sharedStakingState.chainAndAsset()
        val account = accountRepository.getSelectedMetaAccount()

        return accountStakingDao.get(chain.id, chainAsset.id, account.accountIdIn(chain)!!)
    }
}
