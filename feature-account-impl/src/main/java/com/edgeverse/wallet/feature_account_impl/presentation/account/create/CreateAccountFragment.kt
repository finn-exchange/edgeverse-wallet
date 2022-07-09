package com.edgeverse.wallet.feature_account_impl.presentation.account.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.hideSoftKeyboard
import com.edgeverse.wallet.common.utils.nameInputFilters
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.di.AccountFeatureComponent
import com.edgeverse.wallet.feature_account_impl.presentation.common.mixin.impl.setupAccountNameChooserUi
import kotlinx.android.synthetic.main.fragment_create_account.createAccountNameField
import kotlinx.android.synthetic.main.fragment_create_account.nextBtn
import kotlinx.android.synthetic.main.fragment_create_account.toolbar
import javax.inject.Inject

class CreateAccountFragment : BaseFragment<CreateAccountViewModel>() {

    companion object {

        private const val PAYLOAD = "CreateAccountFragment.payload"

        fun getBundle(payload: AddAccountPayload.MetaAccount): Bundle {

            return Bundle().apply {
                putParcelable(PAYLOAD, payload)
            }
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun initViews() {
        toolbar.setHomeButtonListener { viewModel.homeButtonClicked() }

        nextBtn.setOnClickListener {
            createAccountNameField.hideSoftKeyboard()
            viewModel.nextClicked()
        }

        createAccountNameField.content.filters = nameInputFilters()
    }

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .createAccountComponentFactory()
            .create(this, argument(PAYLOAD))
            .inject(this)
    }

    override fun subscribe(viewModel: CreateAccountViewModel) {
        viewModel.nextButtonEnabledLiveData.observe {
            nextBtn.isEnabled = it
        }

        setupAccountNameChooserUi(viewModel, ui = createAccountNameField)
    }
}
