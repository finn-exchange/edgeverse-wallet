package com.edgeverse.wallet.common.view

import com.edgeverse.wallet.common.address.AddressModel

fun LabeledTextView.setAddressModel(addressModel: AddressModel) {
    setTextIcon(addressModel.image)
    setMessage(addressModel.nameOrAddress)
}
