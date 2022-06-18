package com.dfinn.wallet.feature_wallet_api.presentation.view.extrinsic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.dfinn.wallet.common.address.AddressModel
import com.dfinn.wallet.common.view.TableView
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletModel
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.showWallet
import com.dfinn.wallet.feature_account_api.view.showAddress
import com.dfinn.wallet.feature_wallet_api.R
import com.dfinn.wallet.feature_wallet_api.presentation.mixin.fee.FeeStatus
import com.dfinn.wallet.feature_wallet_api.presentation.view.FeeView
import kotlinx.android.synthetic.main.view_generic_extrinsic_information.view.viewGenericExtrinsicInformationAccount
import kotlinx.android.synthetic.main.view_generic_extrinsic_information.view.viewGenericExtrinsicInformationFee
import kotlinx.android.synthetic.main.view_generic_extrinsic_information.view.viewGenericExtrinsicInformationWallet

class GenericExtrinsicInformationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : TableView(context, attrs, defStyle) {

    val fee: FeeView
        get() = viewGenericExtrinsicInformationFee

    init {
        View.inflate(context, R.layout.view_generic_extrinsic_information, this)
    }

    fun setOnAccountClickedListener(action: (View) -> Unit) {
        viewGenericExtrinsicInformationAccount.setOnClickListener(action)
    }

    fun setWallet(walletModel: WalletModel) {
        viewGenericExtrinsicInformationWallet.showWallet(walletModel)
    }

    fun setAccount(addressModel: AddressModel) {
        viewGenericExtrinsicInformationAccount.showAddress(addressModel)
    }

    fun setFeeStatus(feeStatus: FeeStatus) {
        viewGenericExtrinsicInformationFee.setFeeStatus(feeStatus)
    }
}
