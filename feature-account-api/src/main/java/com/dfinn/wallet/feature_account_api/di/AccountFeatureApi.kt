package com.dfinn.wallet.feature_account_api.di

import com.dfinn.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.dfinn.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.dfinn.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.dfinn.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.dfinn.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.dfinn.wallet.feature_account_api.presenatation.mixin.addressInput.AddressInputMixinFactory
import com.dfinn.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin

interface AccountFeatureApi {

    fun provideAccountRepository(): AccountRepository

    fun externalAccountActions(): ExternalActions.Presentation

    fun accountUpdateScope(): AccountUpdateScope

    fun addressDisplayUseCase(): AddressDisplayUseCase

    fun accountUseCase(): SelectedAccountUseCase

    fun extrinsicService(): ExtrinsicService

    fun importTypeChooserMixin(): ImportTypeChooserMixin.Presentation

    val addressInputMixinFactory: AddressInputMixinFactory

    val walletUiUseCase: WalletUiUseCase
}
