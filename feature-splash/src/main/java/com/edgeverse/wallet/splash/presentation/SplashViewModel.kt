package com.edgeverse.wallet.splash.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.edgeverse.wallet.common.base.BaseViewModel
import com.edgeverse.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.edgeverse.wallet.splash.SplashRouter
import kotlinx.coroutines.launch


class SplashViewModel(
    private val router: SplashRouter,
    private val repository: AccountRepository
) : BaseViewModel() {

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            openInitialDestination()
        }, 500)
    }

    private fun openInitialDestination() {
        viewModelScope.launch {
            if (repository.isAccountSelected()) {
                if (repository.isCodeSet()) {
                    router.openInitialCheckPincode()
                } else {
                    router.openCreatePincode()
                }
            } else {
                router.openAddFirstAccount()
            }
        }
    }
}
