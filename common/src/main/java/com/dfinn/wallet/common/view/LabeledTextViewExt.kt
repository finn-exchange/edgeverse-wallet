package com.dfinn.wallet.common.view

import com.dfinn.wallet.common.address.AddressModel

fun LabeledTextView.setAddressModel(addressModel: AddressModel) {
    setTextIcon(addressModel.image)
    setMessage(addressModel.nameOrAddress)
}
