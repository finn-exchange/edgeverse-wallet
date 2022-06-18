package com.dfinn.wallet.feature_account_api.presenatation.account.wallet

import com.dfinn.wallet.common.view.TableCellView

fun TableCellView.showWallet(walletModel: WalletModel) {
    showValue(walletModel.name)

    walletModel.icon?.let(::setImage)
}
