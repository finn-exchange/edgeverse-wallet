package jp.co.soramitsu.feature_staking_impl.domain.validators.current

import jp.co.soramitsu.common.list.GroupedList
import jp.co.soramitsu.common.utils.mapValuesNotNull
import jp.co.soramitsu.common.utils.networkType
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.feature_staking_api.domain.api.StakingRepository
import jp.co.soramitsu.feature_staking_api.domain.model.NominatedValidator
import jp.co.soramitsu.feature_staking_api.domain.model.NominatedValidatorStatus
import jp.co.soramitsu.feature_staking_api.domain.model.StakingState
import jp.co.soramitsu.feature_staking_impl.data.repository.StakingConstantsRepository
import jp.co.soramitsu.feature_staking_impl.domain.common.isWaiting
import jp.co.soramitsu.feature_staking_impl.domain.validators.ValidatorProvider
import jp.co.soramitsu.feature_staking_impl.domain.validators.ValidatorSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrentValidatorsInteractor(
    private val stakingRepository: StakingRepository,
    private val stakingConstantsRepository: StakingConstantsRepository,
    private val validatorProvider: ValidatorProvider,
) {

    suspend fun nominatedValidatorsFlow(
        nominatorState: StakingState.Stash.Nominator,
    ): Flow<GroupedList<NominatedValidatorStatus, NominatedValidator>> {
        val networkType = nominatorState.accountAddress.networkType()

        return stakingRepository.observeActiveEraIndex(networkType).map { activeEra ->
            val stashId = nominatorState.stashId

            val exposures = stakingRepository.getElectedValidatorsExposure(activeEra)
            val activeNominations = exposures.mapValuesNotNull { (_, exposure) ->
                exposure.others.firstOrNull { it.who.contentEquals(stashId) }
            }

            val nominatedValidatorIds = nominatorState.nominations.targets.mapTo(mutableSetOf(), ByteArray::toHexString)

            val allValidators = activeNominations.keys + nominatedValidatorIds

            val isWaitingForNextEra = nominatorState.nominations.isWaiting(activeEra)
            val waitingForNextEraStatus = NominatedValidatorStatus.WaitingForNextEra(stakingConstantsRepository.maxValidatorsPerNominator())

            validatorProvider.getValidators(
                ValidatorSource.Custom(allValidators.toList()),
                cachedExposures = exposures
            ).map {
                NominatedValidator(it, activeNominations[it.accountIdHex]?.value)
            }.groupBy {
                when {
                    it.nominationInPlanks != null -> NominatedValidatorStatus.Active
                    isWaitingForNextEra -> waitingForNextEraStatus
                    exposures[it.validator.accountIdHex] != null -> NominatedValidatorStatus.Elected
                    else -> NominatedValidatorStatus.Inactive
                }
            }.toSortedMap(NominatedValidatorStatus.COMPARATOR)
        }
    }
}
