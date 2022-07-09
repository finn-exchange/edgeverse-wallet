package com.edgeverse.wallet.root.presentation

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.edgeverse.wallet.R
import com.edgeverse.wallet.common.base.BaseActivity
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.EventObserver
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.utils.showToast
import com.edgeverse.wallet.common.utils.systemCall.SystemCallExecutor
import com.edgeverse.wallet.common.utils.updatePadding
import com.edgeverse.wallet.root.di.RootApi
import com.edgeverse.wallet.root.di.RootComponent
import com.edgeverse.wallet.root.navigation.NavigationHolder
import com.edgeverse.wallet.splash.presentation.SplashBackgroundHolder
import kotlinx.android.synthetic.main.activity_root.*
import javax.inject.Inject

class RootActivity : BaseActivity<RootViewModel>(), SplashBackgroundHolder {

    @Inject
    lateinit var navigationHolder: NavigationHolder

    @Inject
    lateinit var systemCallExecutor: SystemCallExecutor

    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .mainActivityComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        removeSplashBackground()

        viewModel.restoredAfterConfigChange()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!systemCallExecutor.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        systemCallExecutor.attachActivity(this)
        navigationHolder.attach(navController, this)

        rootNetworkBar.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(top = insets.systemWindowInsetTop)

            insets
        }

        intent?.let(::processIntent)

//        processJsonOpenIntent()
    }

    override fun onDestroy() {
        super.onDestroy()

        systemCallExecutor.detachActivity()
        navigationHolder.detach()
    }

    override fun layoutResource(): Int {
        return R.layout.activity_root
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        processIntent(intent)
    }

    override fun initViews() {
    }

    override fun onStop() {
        super.onStop()

        viewModel.noticeInBackground()
    }

    override fun onStart() {
        super.onStart()

        viewModel.noticeInForeground()
    }

    override fun subscribe(viewModel: RootViewModel) {
        viewModel.showConnectingBarLiveData.observe(this) { show ->
            rootNetworkBar.setVisible(show)
        }

        viewModel.messageLiveData.observe(
            this,
            EventObserver {
                showToast(it)
            }
        )
    }

    override fun removeSplashBackground() {
        mainView.setBackgroundResource(R.color.black)
    }

    override fun changeLanguage() {
        viewModel.noticeLanguageLanguage()

        recreate()
    }

    private fun processIntent(intent: Intent) {
        val uri = intent.data?.toString()

        uri?.let { viewModel.externalUrlOpened(uri) }
    }

//    private fun processJsonOpenIntent() {
//        if (Intent.ACTION_VIEW == intent.action && intent.type != null) {
//            if ("application/json" == intent.type) {
//                val file = this.contentResolver.openInputStream(intent.data!!)
//                val content = file?.reader(Charsets.UTF_8)?.readText()
//                viewModel.jsonFileOpened(content)
//            }
//        }
//    }

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

        navHostFragment.navController
    }
}
