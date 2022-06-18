package com.dfinn.wallet.feature_dapp_impl.domain.browser.metamask

import com.dfinn.wallet.common.utils.removeHexPrefix
import com.dfinn.wallet.core.model.CryptoType
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.model.addressIn
import com.dfinn.wallet.feature_account_api.domain.model.mainEthereumAddress
import com.dfinn.wallet.feature_dapp_impl.web3.metamask.model.EthereumAddress
import com.dfinn.wallet.runtime.ext.addressOf
import com.dfinn.wallet.runtime.multiNetwork.ChainRegistry
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import com.dfinn.wallet.runtime.multiNetwork.findChain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MetamaskInteractor(
    private val accountRepository: AccountRepository,
    private val chainRegistry: ChainRegistry
) {

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun getAddresses(ethereumChainId: String): List<EthereumAddress> = withContext(Dispatchers.Default) {
        val selectedAccount = accountRepository.getSelectedMetaAccount()
        val maybeChain = tryFindChainFromEthereumChainId(ethereumChainId)

        val chainsById = chainRegistry.chainsById.first()

        val selectedAddress = maybeChain?.let { selectedAccount.addressIn(it) }

        val mainAddress = selectedAccount.mainEthereumAddress()

        val chainAccountAddresses = selectedAccount.chainAccounts
            .mapNotNull { (chainId, chainAccount) ->
                val chain = chainsById[chainId]

                chain?.addressOf(chainAccount.accountId)?.takeIf {
                    chain.isEthereumBased && chainAccount.cryptoType == CryptoType.ECDSA
                }
            }

        buildList {
            selectedAddress?.let { add(it) }
            mainAddress?.let { add(it) }
            addAll(chainAccountAddresses)
        }.distinct()
    }

    suspend fun tryFindChainFromEthereumChainId(ethereumChainId: String): Chain? {
        val addressPrefix = ethereumChainId.removeHexPrefix().toIntOrNull(radix = 16) ?: return null

        return chainRegistry.findChain { it.isEthereumBased && it.addressPrefix == addressPrefix }
    }
}
