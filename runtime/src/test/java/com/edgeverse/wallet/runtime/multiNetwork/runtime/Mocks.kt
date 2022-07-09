package com.edgeverse.wallet.runtime.multiNetwork.runtime

import com.edgeverse.wallet.runtime.multiNetwork.chain.model.Chain
import com.edgeverse.wallet.test_shared.whenever
import org.mockito.Mockito

object Mocks {
    fun chain(id: String) : Chain {
        val chain = Mockito.mock(Chain::class.java)

        whenever(chain.id).thenReturn(id)

        return chain
    }
}
