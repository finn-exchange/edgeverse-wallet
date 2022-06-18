package com.dfinn.wallet.core_db.di

import android.content.Context
import com.dfinn.wallet.common.data.secrets.v1.SecretStoreV1
import com.dfinn.wallet.common.data.secrets.v2.SecretStoreV2
import com.dfinn.wallet.common.data.storage.Preferences
import com.google.gson.Gson

interface DbDependencies {

    fun gson(): Gson

    fun preferences(): Preferences

    fun context(): Context

    fun secretStoreV1(): SecretStoreV1

    fun secretStoreV2(): SecretStoreV2
}
