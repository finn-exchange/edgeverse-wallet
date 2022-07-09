package com.edgeverse.wallet.feature_crowdloan_impl.presentation.contributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import dev.chrisbanes.insetter.applyInsetter
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_crowdloan_api.di.CrowdloanFeatureApi
import com.edgeverse.wallet.feature_crowdloan_impl.R
import com.edgeverse.wallet.feature_crowdloan_impl.di.CrowdloanFeatureComponent
import kotlinx.android.synthetic.main.fragment_my_contributions.myContributionsContainer
import kotlinx.android.synthetic.main.fragment_my_contributions.myContributionsList
import kotlinx.android.synthetic.main.fragment_my_contributions.myContributionsPlaceholder
import kotlinx.android.synthetic.main.fragment_my_contributions.myContributionsProgress
import kotlinx.android.synthetic.main.fragment_my_contributions.myContributionsToolbar
import javax.inject.Inject

class UserContributionsFragment : BaseFragment<UserContributionsViewModel>() {

    @Inject protected lateinit var imageLoader: ImageLoader

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        UserContributionsAdapter(imageLoader)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_my_contributions, container, false)
    }

    override fun initViews() {
        myContributionsContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        myContributionsList.adapter = adapter
        myContributionsList.setHasFixedSize(true)

        myContributionsToolbar.setHomeButtonListener { viewModel.backClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<CrowdloanFeatureComponent>(
            requireContext(),
            CrowdloanFeatureApi::class.java
        )
            .userContributionsFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: UserContributionsViewModel) {

        viewModel.userContributionsFlow.observe { loadingState ->
            myContributionsList.setVisible(loadingState is LoadingState.Loaded && loadingState.data.isNotEmpty())
            myContributionsPlaceholder.setVisible(loadingState is LoadingState.Loaded && loadingState.data.isEmpty())
            myContributionsProgress.setVisible(loadingState is LoadingState.Loading)

            if (loadingState is LoadingState.Loaded) {
                adapter.submitList(loadingState.data)
            }
        }
    }
}
