package com.dfinn.wallet.feature_crowdloan_impl.domain.contribute.custom.moonbeam

import android.util.Log
import com.dfinn.wallet.common.data.network.HttpExceptionHandler
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.utils.LOG_TAG
import com.dfinn.wallet.common.utils.sha256
import com.dfinn.wallet.core.model.CryptoType
import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.data.secrets.sign
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.model.accountIdIn
import com.dfinn.wallet.feature_account_api.domain.model.addressIn
import com.dfinn.wallet.feature_account_api.domain.model.cryptoTypeIn
import com.dfinn.wallet.feature_account_api.domain.model.hasAccountIn
import com.dfinn.wallet.feature_crowdloan_api.data.repository.ParachainMetadata
import com.dfinn.wallet.feature_crowdloan_impl.data.network.api.moonbeam.*
import com.dfinn.wallet.feature_crowdloan_impl.data.network.blockhain.extrinsic.addMemo
import com.dfinn.wallet.feature_crowdloan_impl.domain.main.Crowdloan
import com.dfinn.wallet.runtime.extrinsic.ExtrinsicStatus
import com.dfinn.wallet.runtime.extrinsic.systemRemark
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.state.SingleAssetSharedState
import com.dfinn.wallet.runtime.state.chain
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.math.BigInteger

class VerificationError : Exception()

private val SUPPORTED_CRYPTO_TYPES = setOf(CryptoType.SR25519, CryptoType.ED25519)

class MoonbeamCrowdloanInteractor(
    private val accountRepository: AccountRepository,
    private val extrinsicService: ExtrinsicService,
    private val moonbeamApi: MoonbeamApi,
    private val selectedChainAssetState: SingleAssetSharedState,
    private val chainRegistry: ChainRegistry,
    private val httpExceptionHandler: HttpExceptionHandler,
    private val secretStoreV2: SecretStoreV2,
) {

    fun getTermsLink() = "https://github.com/moonbeam-foundation/crowdloan-self-attestation/blob/main/moonbeam/README.md"

    suspend fun getMoonbeamRewardDestination(parachainMetadata: ParachainMetadata): CrossChainRewardDestination {
        val currentAccount = accountRepository.getSelectedMetaAccount()
        val moonbeamChain = chainRegistry.getChain(parachainMetadata.moonbeamChainId())

        return CrossChainRewardDestination(
            addressInDestination = currentAccount.addressIn(moonbeamChain)!!,
            destination = moonbeamChain
        )
    }

    suspend fun additionalSubmission(
        crowdloan: Crowdloan,
        extrinsicBuilder: ExtrinsicBuilder,
    ) {
        val rewardDestination = getMoonbeamRewardDestination(crowdloan.parachainMetadata!!)

        extrinsicBuilder.addMemo(
            parachainId = crowdloan.parachainId,
            memo = rewardDestination.addressInDestination.fromHex()
        )
    }

    suspend fun flowStatus(parachainMetadata: ParachainMetadata): Result<MoonbeamFlowStatus> = withContext(Dispatchers.Default) {
        runCatching {
            val metaAccount = accountRepository.getSelectedMetaAccount()

            val moonbeamChainId = parachainMetadata.moonbeamChainId()
            val moonbeamChain = chainRegistry.getChain(moonbeamChainId)

            val currentChain = selectedChainAssetState.chain()
            val currentAddress = metaAccount.addressIn(currentChain)!!

            when {
                !metaAccount.hasAccountIn(moonbeamChain) -> MoonbeamFlowStatus.NeedsChainAccount(
                    chainId = moonbeamChainId,
                    metaId = metaAccount.id
                )

                metaAccount.cryptoTypeIn(currentChain) !in SUPPORTED_CRYPTO_TYPES -> MoonbeamFlowStatus.UnsupportedAccountEncryption

                else -> when (checkRemark(parachainMetadata, currentAddress)) {
                    null -> MoonbeamFlowStatus.RegionNotSupported
                    true -> MoonbeamFlowStatus.Completed
                    false -> MoonbeamFlowStatus.ReadyToComplete
                }
            }
        }
    }

    suspend fun calculateTermsFee(): BigInteger = withContext(Dispatchers.Default) {
        val chain = selectedChainAssetState.chain()

        extrinsicService.estimateFee(chain) {
            systemRemark(fakeRemark())
        }
    }

    suspend fun submitAgreement(parachainMetadata: ParachainMetadata): Result<*> = withContext(Dispatchers.Default) {
        runCatching {
            val chain = selectedChainAssetState.chain()
            val metaAccount = accountRepository.getSelectedMetaAccount()

            val currentAddress = metaAccount.addressIn(chain)!!

            val legalText = httpExceptionHandler.wrap { moonbeamApi.getLegalText() }
            val legalHash = legalText.encodeToByteArray().sha256().toHexString(withPrefix = false)
            val signedHash = secretStoreV2.sign(metaAccount, chain, legalHash)

            val agreeRemarkRequest = AgreeRemarkRequest(currentAddress, signedHash)
            val remark = httpExceptionHandler.wrap { moonbeamApi.agreeRemark(parachainMetadata, agreeRemarkRequest) }.remark

            val finalizedStatus = extrinsicService.submitAndWatchExtrinsic(chain, metaAccount.accountIdIn(chain)!!) {
                systemRemark(remark.encodeToByteArray())
            }
                .filterIsInstance<ExtrinsicStatus.Finalized>()
                .first()

            Log.d(this@MoonbeamCrowdloanInteractor.LOG_TAG, "Finalized ${finalizedStatus.extrinsicHash} in block ${finalizedStatus.blockHash}")

            val verificationRequest = VerifyRemarkRequest(
                address = currentAddress,
                extrinsicHash = finalizedStatus.extrinsicHash,
                blockHash = finalizedStatus.blockHash
            )
            val verificationResponse = httpExceptionHandler.wrap { moonbeamApi.verifyRemark(parachainMetadata, verificationRequest) }

            if (!verificationResponse.verified) throw VerificationError()
        }
    }

    private fun fakeRemark() = ByteArray(32)

    /**
     * @return null if Geo-fenced or application unavailable. True if user already agreed with terms. False otherwise
     */
    private suspend fun checkRemark(parachainMetadata: ParachainMetadata, address: String): Boolean? = try {
        moonbeamApi.checkRemark(parachainMetadata, address).verified
    } catch (e: HttpException) {
        if (e.code() == 403) { // Moonbeam answers with 403 in case geo-fenced or application unavailable
            null
        } else {
            throw httpExceptionHandler.transformException(e)
        }
    } catch (e: Exception) {
        throw httpExceptionHandler.transformException(e)
    }
}
