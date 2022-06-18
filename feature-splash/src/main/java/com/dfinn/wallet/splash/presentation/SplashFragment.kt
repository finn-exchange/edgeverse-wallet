package com.dfinn.wallet.splash.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.splash.R
import com.dfinn.wallet.splash.di.SplashFeatureApi
import com.dfinn.wallet.splash.di.SplashFeatureComponent
import javax.inject.Inject

class SplashFragment : BaseFragment<SplashViewModel>() {

    @Inject lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun initViews() {
    }

    override fun inject() {
        FeatureUtils.getFeature<SplashFeatureComponent>(this, SplashFeatureApi::class.java)
            .splashComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as? SplashBackgroundHolder)?.removeSplashBackground()
    }

    override fun subscribe(viewModel: SplashViewModel) {}
}
