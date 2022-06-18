package com.dfinn.wallet.feature_account_impl.presentation.exporting.json.confirm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_account_impl.di.AccountFeatureComponent
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import javax.inject.Inject

class ShareCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var router: AccountRouter

    override fun onReceive(context: Context, intent: Intent) {
        FeatureUtils.getFeature<AccountFeatureComponent>(context, AccountFeatureApi::class.java)
            .inject(this)

        router.finishExportFlow()
    }
}
