package com.edgeverse.wallet.feature_staking_impl.domain.validators.current.search

import android.annotation.SuppressLint
import com.edgeverse.wallet.feature_staking_api.domain.model.Validator
import com.edgeverse.wallet.feature_staking_impl.data.StakingSharedState
import com.edgeverse.wallet.feature_staking_impl.domain.validators.ValidatorProvider
import com.edgeverse.wallet.runtime.ext.isValidAddress
import com.edgeverse.wallet.runtime.state.chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchCustomValidatorsInteractor(
    private val validatorProvider: ValidatorProvider,
    private val sharedState: StakingSharedState
) {

    @SuppressLint("DefaultLocale")
    suspend fun searchValidator(query: String, localValidators: Collection<Validator>): List<Validator> = withContext(Dispatchers.Default) {
        val queryLower = query.toLowerCase()

        val searchInLocal = localValidators.filter {
            val foundInIdentity = it.identity?.display?.toLowerCase()?.contains(queryLower) ?: false

            it.address.startsWith(query) || foundInIdentity
        }

        if (searchInLocal.isNotEmpty()) {
            return@withContext searchInLocal
        }

        val chain = sharedState.chain()

        if (chain.isValidAddress(query)) {
            val validator = validatorProvider.getValidatorWithoutElectedInfo(chain.id, query)

            if (validator.prefs != null) {
                listOf(validator)
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}
