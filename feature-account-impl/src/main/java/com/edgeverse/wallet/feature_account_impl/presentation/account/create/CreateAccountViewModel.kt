package com.edgeverse.wallet.feature_account_impl.presentation.account.create

import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.common.mixin.MixinFactory
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_impl.data.mappers.mapNameChooserStateToOptionalName
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.AccountNameChooserMixin
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.api.WithAccountNameChooserMixin

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
