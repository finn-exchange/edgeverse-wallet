package com.dfinn.wallet.feature_dapp_impl.presentation.common.favourites

import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.mixin.actionAwaitable.ConfirmationAwaitable
import com.dfinn.wallet.common.view.dialog.warningDialog
import com.dfinn.wallet.feature_dapp_impl.R

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
