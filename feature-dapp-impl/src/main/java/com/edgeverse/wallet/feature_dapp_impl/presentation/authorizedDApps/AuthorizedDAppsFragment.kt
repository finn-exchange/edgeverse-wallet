package com.edgeverse.wallet.feature_dapp_impl.presentation.authorizedDApps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.view.dialog.warningDialog
import com.edgeverse.wallet.feature_account_api.presenatation.account.wallet.showWallet
import com.edgeverse.wallet.feature_dapp_api.di.DAppFeatureApi
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.di.DAppFeatureComponent
import com.edgeverse.wallet.feature_dapp_impl.presentation.authorizedDApps.model.AuthorizedDAppModel
import kotlinx.android.synthetic.main.fragment_authorized_dapps.authorizedDAppsList
import kotlinx.android.synthetic.main.fragment_authorized_dapps.authorizedDAppsToolbar
import kotlinx.android.synthetic.main.fragment_authorized_dapps.authorizedDAppsWallet
import kotlinx.android.synthetic.main.fragment_authorized_dapps.authorizedPlaceholder
import kotlinx.android.synthetic.main.fragment_authorized_dapps.authorizedPlaceholderSpacerBottom
import kotlinx.android.synthetic.main.fragment_authorized_dapps.authorizedPlaceholderSpacerTop
import javax.inject.Inject

class AuthorizedDAppsFragment : BaseFragment<AuthorizedDAppsViewModel>(), AuthorizedDAppAdapter.Handler {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AuthorizedDAppAdapter(this, imageLoader)
    }

    private val placeholderViews by lazy(LazyThreadSafetyMode.NONE) {
        listOf(authorizedPlaceholderSpacerTop, authorizedPlaceholder, authorizedPlaceholderSpacerBottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_authorized_dapps, container, false)
    }

    override fun initViews() {
        authorizedDAppsToolbar.applyStatusBarInsets()
        authorizedDAppsToolbar.setHomeButtonListener { viewModel.backClicked() }

        authorizedDAppsList.setHasFixedSize(true)
        authorizedDAppsList.adapter = adapter
    }

    override fun inject() {
        FeatureUtils.getFeature<DAppFeatureComponent>(this, DAppFeatureApi::class.java)
            .authorizedDAppsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: AuthorizedDAppsViewModel) {
        viewModel.authorizedDApps.observe {
            val showPlaceholder = it.isEmpty()

            authorizedDAppsList.setVisible(!showPlaceholder)
            placeholderViews.forEach { view -> view.setVisible(showPlaceholder) }

            adapter.submitList(it)
        }

        viewModel.walletUi.observe {
            authorizedDAppsWallet.showWallet(it)
        }

        viewModel.revokeAuthorizationConfirmation.awaitableActionLiveData.observeEvent {
            warningDialog(
                context = requireContext(),
                onConfirm = { it.onSuccess(Unit) },
                onCancel = it.onCancel,
                confirmTextRes = R.string.common_remove
            ) {
                setTitle(R.string.dapp_authorized_remove_title)
                setMessage(getString(R.string.dapp_authorized_remove_description, it.payload))
            }
        }
    }

    override fun onRevokeClicked(item: AuthorizedDAppModel) {
        viewModel.revokeClicked(item)
    }
}
