package com.dfinn.wallet.feature_dapp_impl.domain.browser.signExtrinsic

import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.feature_account_api.presenatation.chain.ChainUi
import com.dfinn.wallet.feature_dapp_impl.presentation.browser.signExtrinsic.DAppSignCommunicator
import com.dfinn.wallet.feature_wallet_api.domain.model.Token
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

interface DAppSignInteractor {

    val validationSystem: ConfirmDAppOperationValidationSystem

    suspend fun createAccountAddressModel(): AddressModel

    suspend fun chainUi(): ChainUi?

    fun commissionTokenFlow(): Flow<Token>?

    suspend fun calculateFee(): BigInteger

    suspend fun performOperation(): DAppSignCommunicator.Response

    suspend fun readableOperationContent(): String

    fun shutdown() {}
}
