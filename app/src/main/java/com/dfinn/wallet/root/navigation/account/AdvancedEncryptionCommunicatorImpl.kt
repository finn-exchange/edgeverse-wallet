package com.dfinn.wallet.root.navigation.account

import com.dfinn.wallet.R
import com.dfinn.wallet.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import com.dfinn.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionFragment
import com.dfinn.wallet.feature_account_impl.presentation.account.advancedEncryption.AdvancedEncryptionPayload
import com.dfinn.wallet.root.navigation.BaseInterScreenCommunicator
import com.dfinn.wallet.root.navigation.NavigationHolder

class AdvancedEncryptionCommunicatorImpl(
    navigationHolder: NavigationHolder
) : BaseInterScreenCommunicator<AdvancedEncryptionPayload, AdvancedEncryptionCommunicator.Response>(navigationHolder), AdvancedEncryptionCommunicator {

    override fun openRequest(request: AdvancedEncryptionPayload) {
        navController.navigate(R.id.action_open_advancedEncryptionFragment, AdvancedEncryptionFragment.getBundle(request))
    }
}
