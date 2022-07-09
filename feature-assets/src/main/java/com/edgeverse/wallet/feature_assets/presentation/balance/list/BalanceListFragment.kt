package com.edgeverse.wallet.feature_assets.presentation.balance.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import coil.ImageLoader
import dev.chrisbanes.insetter.applyInsetter
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.hideKeyboard
import com.edgeverse.wallet.common.view.shape.addRipple
import com.edgeverse.wallet.common.view.shape.getRoundedCornerDrawable
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureComponent
import com.edgeverse.wallet.feature_assets.presentation.balance.list.view.AssetGroupingDecoration
import com.edgeverse.wallet.feature_assets.presentation.balance.list.view.AssetsHeaderAdapter
import com.edgeverse.wallet.feature_assets.presentation.balance.list.view.BalanceListAdapter
import com.edgeverse.wallet.feature_assets.presentation.model.AssetModel
import kotlinx.android.synthetic.main.fragment_balance_list.balanceListAssets
import kotlinx.android.synthetic.main.fragment_balance_list.walletContainer
import javax.inject.Inject

class BalanceListFragment :
    BaseFragment<BalanceListViewModel>(),
    BalanceListAdapter.ItemAssetHandler,
    AssetsHeaderAdapter.Handler {

    @Inject
    protected lateinit var imageLoader: ImageLoader

    private val assetsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        BalanceListAdapter(imageLoader, this)
    }

    private val headerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AssetsHeaderAdapter(this)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ConcatAdapter(headerAdapter, assetsAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_balance_list, container, false)
    }

    override fun initViews() {
        balanceListAssets.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        hideKeyboard()

        balanceListAssets.setHasFixedSize(true)
        balanceListAssets.adapter = adapter

        val groupBackground = with(requireContext()) {
            addRipple(getRoundedCornerDrawable(R.color.blurColor))
        }
        val decoration = AssetGroupingDecoration(
            background = groupBackground,
            assetsAdapter = assetsAdapter,
            context = requireContext(),
        )
        balanceListAssets.addItemDecoration(decoration)
        // modification animations only harm here
        balanceListAssets.itemAnimator = null

        walletContainer.setOnRefreshListener {
            viewModel.fullSync()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(
            requireContext(),
            AssetsFeatureApi::class.java
        )
            .balanceListComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: BalanceListViewModel) {
        viewModel.assetsFlow.observe {
            assetsAdapter.submitList(it) {
                balanceListAssets.invalidateItemDecorations()
            }
        }

        viewModel.totalBalanceFlow.observe(headerAdapter::setTotalBalance)
        viewModel.currentAddressModelFlow.observe(headerAdapter::setAddress)

        viewModel.nftCountFlow.observe(headerAdapter::setNftCountLabel)
        viewModel.nftPreviewsUi.observe(headerAdapter::setNftPreviews)

        viewModel.hideRefreshEvent.observeEvent {
            walletContainer.isRefreshing = false
        }
    }

    override fun assetClicked(asset: AssetModel) {
        viewModel.assetClicked(asset)
    }

    override fun manageClicked() {
        viewModel.manageClicked()
    }

    override fun avatarClicked() {
        viewModel.avatarClicked()
    }

    override fun goToNftsClicked() {
        viewModel.goToNftsClicked()
    }
}
