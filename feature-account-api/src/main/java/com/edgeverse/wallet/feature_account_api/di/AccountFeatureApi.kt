package com.edgeverse.wallet.feature_account_api.di

import com.edgeverse.wallet.feature_account_api.data.extrinsic.ExtrinsicService
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.feature_account_api.domain.interfaces.SelectedAccountUseCase
import com.edgeverse.wallet.feature_account_api.domain.updaters.AccountUpdateScope
import com.edgeverse.wallet.feature_account_api.presenatation.account.AddressDisplayUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.WalletUiUseCase
import com.edgeverse.wallet.feature_account_api.presenatation.actions.ExternalActions
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.addressInput.AddressInputMixinFactory
import com.edgeverse.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin

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
