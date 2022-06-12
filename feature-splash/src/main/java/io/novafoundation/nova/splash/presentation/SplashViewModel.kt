package io.novafoundation.nova.splash.presentation

import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.viewModelScope
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.splash.SplashRouter
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
