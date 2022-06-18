package com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion

import com.dfinn.wallet.common.base.TitleAndMessage
import com.dfinn.wallet.common.resources.ResourceManager
import com.dfinn.wallet.feature_account_impl.R
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationFailure.ETHEREUM_DERIVATION_PATH
import com.dfinn.wallet.feature_account_impl.domain.account.advancedEncryption.valiadtion.AdvancedEncryptionValidationFailure.SUBSTRATE_DERIVATION_PATH

enum class AdvancedEncryptionValidationFailure {
    ETHEREUM_DERIVATION_PATH, SUBSTRATE_DERIVATION_PATH
}

fun mapAdvancedEncryptionValidationFailureToUi(
    resourceManager: ResourceManager,
    failure: AdvancedEncryptionValidationFailure,
): TitleAndMessage {

    return when (failure) {
        SUBSTRATE_DERIVATION_PATH -> resourceManager.getString(R.string.account_derivation_path_substrate_invalid_title) to
            resourceManager.getString(R.string.account_invalid_derivation_path_message_v2_2_0)

        ETHEREUM_DERIVATION_PATH -> resourceManager.getString(R.string.account_derivation_path_ethereum_invalid_title) to
            resourceManager.getString(R.string.account_invalid_derivation_path_message_v2_2_0)
    }
}
