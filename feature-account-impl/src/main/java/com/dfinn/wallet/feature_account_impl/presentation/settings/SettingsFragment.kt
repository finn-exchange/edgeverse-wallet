package com.dfinn.wallet.feature_account_impl.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.data.network.coingecko.FiatCurrency
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeBrowserEvents
import com.dfinn.wallet.common.presentation.FiatCurrenciesChooserBottomSheetDialog
import com.dfinn.wallet.common.utils.applyStatusBarInsets
import com.dfinn.wallet.common.utils.sendEmailIntent
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_account_impl.R
import com.dfinn.wallet.feature_account_impl.di.AccountFeatureComponent
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class SettingsFragment : BaseFragment<SettingsViewModel>() {

    @Inject
    protected lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun initViews() {
        settingsContainer.applyStatusBarInsets()

        accountView.setWholeClickListener { viewModel.accountActionsClicked() }

        settingsWallets.setOnClickListener { viewModel.walletsClicked() }

        settingsNetworks.setOnClickListener { viewModel.networksClicked() }

        settingsLanguage.setOnClickListener { viewModel.languagesClicked() }

        settingsWebsite.setOnClickListener { viewModel.websiteClicked() }

        settingsGithub.setOnClickListener { viewModel.githubClicked() }

        settingsTerms.setOnClickListener { viewModel.termsClicked() }

        settingsPrivacy.setOnClickListener { viewModel.privacyClicked() }

        settingsPin.setOnClickListener { viewModel.changePinCodeClicked() }

        settingsCurrency.setOnClickListener { viewModel.currencyClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .profileComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: SettingsViewModel) {
        observeBrowserEvents(viewModel)

        viewModel.selectedAccountFlow.observe { account ->
            accountView.setTitle(account.name)
        }

        viewModel.accountIconFlow.observe(accountView::setAccountIcon)

        viewModel.selectedLanguageFlow.observe {
            settingsLanguage.setValue(it.displayName)
        }

        viewModel.showFiatChooser.observeEvent(::showFiatChooser)

        viewModel.selectedFiatLiveData.observe {
            settingsCurrency.setValue(it)
        }


        viewModel.openEmailEvent.observeEvent { requireContext().sendEmailIntent(it) }
    }

    private fun showFiatChooser(payload: DynamicListBottomSheet.Payload<FiatCurrency>) {
        FiatCurrenciesChooserBottomSheetDialog(requireContext(), imageLoader, payload, viewModel::onFiatSelected).show()
    }

}
