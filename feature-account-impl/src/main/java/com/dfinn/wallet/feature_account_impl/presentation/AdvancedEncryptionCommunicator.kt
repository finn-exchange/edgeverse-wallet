package com.dfinn.wallet.feature_account_impl.presentation

import android.os.Parcelable
import com.dfinn.wallet.common.navigation.InterScreenRequester
import com.dfinn.wallet.common.navigation.InterScreenResponder
import com.dfinn.wallet.core.model.CryptoType
import com.dfinn.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.dfinn.wallet.feature_account_impl.data.mappers.mapAdvancedEncryptionResponseToAdvancedEncryption
import com.dfinn.wallet.feature_account_impl.data.mappers.mapAdvancedEncryptionStateToResponse
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryption
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import com.dfinn.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator.Response
import com.dfinn.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import kotlinx.android.parcel.Parcelize

interface AdvancedEncryptionRequester : InterScreenRequester<AdvancedEncryptionPayload, Response>

suspend fun AdvancedEncryptionRequester.lastResponseOrDefault(addAccountPayload: AddAccountPayload, using: AdvancedEncryptionInteractor): Response {
    return latestResponse ?: mapAdvancedEncryptionStateToResponse(
        using.getInitialInputState(
            AdvancedEncryptionPayload.Change(addAccountPayload)
        )
    )
}

suspend fun AdvancedEncryptionRequester.lastAdvancedEncryptionOrDefault(
    addAccountPayload: AddAccountPayload,
    using: AdvancedEncryptionInteractor
): AdvancedEncryption {
    return mapAdvancedEncryptionResponseToAdvancedEncryption(lastResponseOrDefault(addAccountPayload, using))
}

interface AdvancedEncryptionResponder : InterScreenResponder<AdvancedEncryptionPayload, Response>

interface AdvancedEncryptionCommunicator : AdvancedEncryptionRequester, AdvancedEncryptionResponder {

    @Parcelize
    class Response(
        val substrateCryptoType: CryptoType?,
        val substrateDerivationPath: String?,
        val ethereumCryptoType: CryptoType?,
        val ethereumDerivationPath: String?
    ) : Parcelable
}
