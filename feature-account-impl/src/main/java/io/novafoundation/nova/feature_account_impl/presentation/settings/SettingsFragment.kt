package io.novafoundation.nova.feature_account_impl.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.mixin.impl.observeBrowserEvents
import io.novafoundation.nova.common.utils.applyStatusBarInsets
import io.novafoundation.nova.common.utils.sendEmailIntent
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.android.synthetic.main.fragment_profile.*

class SettingsFragment : BaseFragment<SettingsViewModel>() {

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

        settingsCurrency.setOnClickListener {
            // TODO to implement
        }
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

        viewModel.openEmailEvent.observeEvent { requireContext().sendEmailIntent(it) }
    }
}
