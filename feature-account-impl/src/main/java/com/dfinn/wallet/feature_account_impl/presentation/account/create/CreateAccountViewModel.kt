package com.dfinn.wallet.feature_account_impl.presentation.account.create

import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.mixin.MixinFactory
import com.dfinn.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.dfinn.wallet.feature_account_impl.data.mappers.mapNameChooserStateToOptionalName
import com.dfinn.wallet.feature_account_impl.presentation.AccountRouter
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.dfinn.wallet.feature_account_impl.presentation.common.mixin.api.WithAccountNameChooserMixin

class CreateAccountViewModel(
    private val router: AccountRouter,
    private val payload: AddAccountPayload,
    accountNameChooserFactory: MixinFactory<AccountNameChooserMixin.Presentation>,
) : BaseViewModel(),
    WithAccountNameChooserMixin {

    override val accountNameChooser: AccountNameChooserMixin.Presentation = accountNameChooserFactory.create(scope = this)

    val nextButtonEnabledLiveData = accountNameChooser.nameValid

    fun homeButtonClicked() {
        router.back()
    }

    fun nextClicked() {
        val nameState = accountNameChooser.nameState.value!!

        router.openMnemonicScreen(mapNameChooserStateToOptionalName(nameState), payload)
    }
}
