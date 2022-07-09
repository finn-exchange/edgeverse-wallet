package com.edgeverse.wallet.feature_account_impl.presentation.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseActivity
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.di.AccountFeatureComponent
import com.edgeverse.wallet.feature_account_impl.presentation.language.model.LanguageModel
import kotlinx.android.synthetic.main.fragment_accounts.novaToolbar
import kotlinx.android.synthetic.main.fragment_languages.languagesList

class LanguagesFragment : BaseFragment<LanguagesViewModel>(), LanguagesAdapter.LanguagesItemHandler {

    private lateinit var adapter: LanguagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_languages, container, false)

    override fun initViews() {
        adapter = LanguagesAdapter(this)

        languagesList.setHasFixedSize(true)
        languagesList.adapter = adapter

        novaToolbar.setHomeButtonListener {
            viewModel.backClicked()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .languagesComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: LanguagesViewModel) {
        adapter.submitList(viewModel.languagesModels)

        viewModel.selectedLanguageLiveData.observe(adapter::updateSelectedLanguage)

        viewModel.languageChangedEvent.observeEvent {
            (activity as BaseActivity<*>).changeLanguage()
        }
    }

    override fun checkClicked(languageModel: LanguageModel) {
        viewModel.selectLanguageClicked(languageModel)
    }
}
