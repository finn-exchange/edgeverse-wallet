package com.edgeverse.wallet.feature_dapp_impl.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.mixin.impl.observeBrowserEvents
import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.feature_dapp_api.di.DAppFeatureApi
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.di.DAppFeatureComponent
import com.edgeverse.wallet.feature_dapp_impl.presentation.common.DappListAdapter
import com.edgeverse.wallet.feature_dapp_impl.presentation.common.DappModel
import com.edgeverse.wallet.feature_dapp_impl.presentation.common.favourites.setupRemoveFavouritesConfirmation
import kotlinx.android.synthetic.main.fragment_dapp_main.dappMainCategorizedDapps
import kotlinx.android.synthetic.main.fragment_dapp_main.dappMainContainer
import kotlinx.android.synthetic.main.fragment_dapp_main.dappMainIcon
import kotlinx.android.synthetic.main.fragment_dapp_main.dappMainManage
import kotlinx.android.synthetic.main.fragment_dapp_main.dappMainSearch

class MainDAppFragment : BaseFragment<MainDAppViewModel>(), DappListAdapter.Handler {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_dapp_main, container, false)
    }

    override fun initViews() {
        dappMainContainer.applyStatusBarInsets()

        dappMainIcon.setOnClickListener { viewModel.accountIconClicked() }

        dappMainCategorizedDapps.setOnCategoryChangedListener {
            viewModel.categorySelected(it)
        }
        dappMainCategorizedDapps.setOnDappListEventsHandler(this)

        dappMainSearch.setOnClickListener {
            viewModel.searchClicked()
        }

        dappMainManage.setOnClickListener {
            viewModel.manageClicked()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<DAppFeatureComponent>(this, DAppFeatureApi::class.java)
            .mainComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: MainDAppViewModel) {
        observeBrowserEvents(viewModel)
        setupRemoveFavouritesConfirmation(viewModel.removeFavouriteConfirmationAwaitable)

        viewModel.currentAddressIconFlow.observe(dappMainIcon::setImageDrawable)

        viewModel.shownDAppsStateFlow.observe { state ->
            when (state) {
                is LoadingState.Loaded -> dappMainCategorizedDapps.showDapps(state.data)
                is LoadingState.Loading -> dappMainCategorizedDapps.showDappsShimmering()
            }
        }

        viewModel.categoriesStateFlow.observe { state ->
            when (state) {
                is LoadingState.Loaded -> dappMainCategorizedDapps.showCategories(state.data)
                is LoadingState.Loading -> dappMainCategorizedDapps.showCategoriesShimmering()
            }
        }
    }

    override fun onItemClicked(item: DappModel) {
        viewModel.dappClicked(item)
    }

    override fun onItemFavouriteClicked(item: DappModel) {
        viewModel.dappFavouriteClicked(item)
    }
}
