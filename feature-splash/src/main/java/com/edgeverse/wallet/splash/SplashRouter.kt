package com.edgeverse.wallet.splash

import com.edgeverse.wallet.common.navigation.SecureRouter

interface SplashRouter : SecureRouter {

    fun openAddFirstAccount()

    fun openCreatePincode()

    fun openInitialCheckPincode()
}
