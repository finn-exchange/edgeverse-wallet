package com.dfinn.wallet.runtime.multiNetwork.qr

import com.dfinn.wallet.runtime.ext.isValidAddress
import com.dfinn.wallet.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.encrypt.qr.QrSharing
import jp.co.soramitsu.fearless_utils.encrypt.qr.formats.AddressQrFormat
import jp.co.soramitsu.fearless_utils.encrypt.qr.formats.SubstrateQrFormat

class MultiChainQrSharingFactory {

    fun create(chain: Chain): QrSharing {
        val mainFormat = SubstrateQrFormat()

        val formats = listOf(
            mainFormat,
            AddressQrFormat(addressValidator = chain::isValidAddress)
        )

        return QrSharing(
            decodingFormats = formats,
            encodingFormat = mainFormat
        )
    }
}
