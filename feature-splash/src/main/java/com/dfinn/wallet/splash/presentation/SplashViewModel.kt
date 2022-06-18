package com.dfinn.wallet.splash.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.dfinn.wallet.common.base.BaseViewModel
import com.dfinn.wallet.feature_account_api.domain.interfaces.AccountRepository
import com.dfinn.wallet.splash.SplashRouter
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
