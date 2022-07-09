package com.edgeverse.wallet.runtime.di

import android.content.Context
import com.google.gson.Gson
import com.edgeverse.wallet.common.data.network.NetworkApiCreator
import com.edgeverse.wallet.common.data.network.rpc.BulkRetriever
import com.edgeverse.wallet.common.data.storage.Preferences
import com.edgeverse.wallet.common.interfaces.FileProvider
import com.edgeverse.wallet.core_db.dao.ChainDao
import com.edgeverse.wallet.core_db.dao.StorageDao
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService

interface RuntimeDependencies {

    fun networkApiCreator(): NetworkApiCreator

    fun socketServiceCreator(): SocketService

    fun gson(): Gson

    fun preferences(): Preferences

    fun fileProvider(): FileProvider

    fun context(): Context

    fun storageDao(): StorageDao

    fun bulkRetriever(): BulkRetriever

    fun chainDao(): ChainDao
}
