package com.edgeverse.wallet.root.navigation.account

import com.edgeverse.wallet.R
import com.edgeverse.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionFragment
import com.edgeverse.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import com.edgeverse.wallet.root.navigation.BaseInterScreenCommunicator
import com.edgeverse.wallet.root.navigation.NavigationHolder

class AdvancedEncryptionCommunicatorImpl(
    navigationHolder: NavigationHolder
) : BaseInterScreenCommunicator<AdvancedEncryptionPayload, AdvancedEncryptionCommunicator.Response>(navigationHolder), AdvancedEncryptionCommunicator {

    override fun openRequest(request: AdvancedEncryptionPayload) {
        navController.navigate(R.id.action_open_advancedEncryptionFragment, AdvancedEncryptionFragment.getBundle(request))
    }
}
