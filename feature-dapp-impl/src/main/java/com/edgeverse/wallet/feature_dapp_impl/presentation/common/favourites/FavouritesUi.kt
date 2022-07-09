package com.edgeverse.wallet.feature_dapp_impl.presentation.common.favourites

import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.mixin.actionAwaitable.ConfirmationAwaitable
import com.edgeverse.wallet.common.view.dialog.warningDialog
import com.edgeverse.wallet.feature_dapp_impl.R

typealias RemoveFavouritesPayload = String // dApp title

fun BaseFragment<*>.setupRemoveFavouritesConfirmation(awaitableMixin: ConfirmationAwaitable<RemoveFavouritesPayload>) {
    awaitableMixin.awaitableActionLiveData.observeEvent {
        warningDialog(
            context = requireContext(),
            onConfirm = { it.onSuccess(Unit) },
            confirmTextRes = R.string.common_remove,
            onCancel = it.onCancel
        ) {
            setTitle(R.string.dapp_favourites_remove_title)

            setMessage(getString(R.string.dapp_favourites_remove_description, it.payload))
        }
    }
}
