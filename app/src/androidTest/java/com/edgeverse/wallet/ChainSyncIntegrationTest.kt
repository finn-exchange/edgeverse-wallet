package com.edgeverse.wallet

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dfinn.wallet.common.data.network.NetworkApiCreator
import com.dfinn.wallet.common.di.CommonApi
import com.dfinn.wallet.common.di.FeatureContainer
import com.dfinn.wallet.core_db.AppDatabase
import com.dfinn.wallet.runtime.multiNetwork.chain.ChainSyncService
import com.dfinn.wallet.runtime.multiNetwork.chain.remote.ChainFetcher
import com.google.gson.Gson
import dagger.Component
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@Component(
    dependencies = [
        CommonApi::class,
    ]
)
interface TestAppComponent {

    fun inject(test: ChainSyncServiceIntegrationTest)
}

@RunWith(AndroidJUnit4::class)
class ChainSyncServiceIntegrationTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val featureContainer = context as FeatureContainer

    @Inject
    lateinit var networkApiCreator: NetworkApiCreator

    lateinit var chainSyncService: ChainSyncService

    @Before
    fun setup() {
        val component = DaggerTestAppComponent.builder()
            .commonApi(featureContainer.commonApi())
            .build()

        component.inject(this)

        val chainDao = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
            .chainDao()

        chainSyncService = ChainSyncService(chainDao, networkApiCreator.create(ChainFetcher::class.java), Gson())
    }

    @Test
    fun shouldFetchAndStoreRealChains() = runBlocking {
        chainSyncService.syncUp()
    }
}
