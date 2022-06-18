package com.dfinn.wallet.feature_onboarding_impl.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.common.data.network.AppLinksProvider
import com.dfinn.wallet.common.mixin.api.Browserable
import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.dfinn.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.dfinn.wallet.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import com.dfinn.wallet.feature_onboarding_impl.OnboardingRouter

class WelcomeViewModel(
    shouldShowBack: Boolean,
    private val router: OnboardingRouter,
    private val appLinksProvider: AppLinksProvider,
    private val addAccountPayload: AddAccountPayload,
    private val importTypeChooserMixin: ImportTypeChooserMixin.Presentation,
) : BaseViewModel(),
    ImportTypeChooserMixin by importTypeChooserMixin,
    Browserable {

    val shouldShowBackLiveData: LiveData<Boolean> = MutableLiveData(shouldShowBack)

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    fun createAccountClicked() {
        when (addAccountPayload) {
            is AddAccountPayload.MetaAccount -> router.openCreateAccount(addAccountPayload)
            is AddAccountPayload.ChainAccount -> router.openMnemonicScreen(accountName = null, addAccountPayload)
        }
    }

    fun importAccountClicked() {
        val payload = ImportTypeChooserMixin.Payload(
            onChosen = { router.openImportAccountScreen(ImportAccountPayload(it, addAccountPayload)) }
        )

        importTypeChooserMixin.showChooser(payload)
    }

    fun termsClicked() {
        openBrowserEvent.value = Event(appLinksProvider.termsUrl)
    }

    fun privacyClicked() {
        openBrowserEvent.value = Event(appLinksProvider.privacyUrl)
    }

    fun backClicked() {
        router.back()
    }
}
