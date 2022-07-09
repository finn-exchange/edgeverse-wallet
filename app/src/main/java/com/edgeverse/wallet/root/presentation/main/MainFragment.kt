package com.edgeverse.wallet.root.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.edgeverse.wallet.R
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.updatePadding
import com.edgeverse.wallet.root.di.RootApi
import com.edgeverse.wallet.root.di.RootComponent
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainViewModel>() {

    private var navController: NavController? = null

    private val backCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            isEnabled = navController!!.navigateUp()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        backCallback.isEnabled = false
    }

    override fun initViews() {
        bottomNavigationView.setOnApplyWindowInsetsListener { _, insets ->
            // overwrite BottomNavigation behavior and ignore insets
            insets
        }

        bottomNavHost.setOnApplyWindowInsetsListener { v, insets ->
            val systemWindowInsetBottom = insets.systemWindowInsetBottom

            // post to prevent bottomNavigationView.height being 0 if callback is called before view has been measured
            v.post {
                val padding = (systemWindowInsetBottom - bottomNavigationView.height).coerceAtLeast(0)
                v.updatePadding(bottom = padding)
            }

            insets
        }

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.bottomNavHost) as NavHostFragment

        navController = nestedNavHostFragment.navController

        bottomNavigationView.setupWithNavController(navController!!)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            onNavDestinationSelected(item, navController!!)
        }
        bottomNavigationView.itemIconTintList = null

        requireActivity().onBackPressedDispatcher.addCallback(backCallback)

        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            backCallback.isEnabled = !isAtHomeTab(destination)
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .mainFragmentComponentFactory()
            .create(requireActivity())
            .inject(this)
    }

    override fun subscribe(viewModel: MainViewModel) {}

    private fun isAtHomeTab(destination: NavDestination) =
        destination.id == navController!!.graph.startDestination
}
