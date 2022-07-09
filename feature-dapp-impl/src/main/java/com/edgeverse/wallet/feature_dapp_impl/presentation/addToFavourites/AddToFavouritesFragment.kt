package com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.applyStatusBarInsets
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.common.utils.hideKeyboard
import com.edgeverse.wallet.common.utils.moveCursorToTheEnd
import com.edgeverse.wallet.common.utils.postToSelf
import com.edgeverse.wallet.common.utils.showSoftKeyboard
import com.edgeverse.wallet.feature_dapp_api.di.DAppFeatureApi
import com.edgeverse.wallet.feature_dapp_impl.R
import com.edgeverse.wallet.feature_dapp_impl.di.DAppFeatureComponent
import com.edgeverse.wallet.feature_dapp_impl.presentation.common.showDAppIcon
import kotlinx.android.synthetic.main.fragment_add_to_favourites.addToFavouritesAddressInput
import kotlinx.android.synthetic.main.fragment_add_to_favourites.addToFavouritesIcon
import kotlinx.android.synthetic.main.fragment_add_to_favourites.addToFavouritesTitleInput
import kotlinx.android.synthetic.main.fragment_add_to_favourites.addToFavouritesToolbar
import javax.inject.Inject

private const val PAYLOAD_KEY = "DAppSignExtrinsicFragment.Payload"

class AddToFavouritesFragment : BaseFragment<AddToFavouritesViewModel>() {

    companion object {

        fun getBundle(payload: AddToFavouritesPayload) = bundleOf(PAYLOAD_KEY to payload)
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_add_to_favourites, container, false)
    }

    override fun initViews() {
        addToFavouritesToolbar.applyStatusBarInsets()
        addToFavouritesToolbar.setHomeButtonListener {
            viewModel.backClicked()
        }

        addToFavouritesToolbar.setRightActionClickListener { viewModel.saveClicked() }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        hideKeyboard()
    }

    override fun inject() {
        FeatureUtils.getFeature<DAppFeatureComponent>(this, DAppFeatureApi::class.java)
            .addToFavouritesComponentFactory()
            .create(this, argument(PAYLOAD_KEY))
            .inject(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun subscribe(viewModel: AddToFavouritesViewModel) {
        addToFavouritesTitleInput.bindTo(viewModel.labelFlow, lifecycleScope)
        addToFavouritesAddressInput.bindTo(viewModel.urlFlow, lifecycleScope)

        viewModel.iconLink.observe {
            addToFavouritesIcon.showDAppIcon(it, imageLoader)
        }

        viewModel.focusOnAddressFieldEvent.observeEvent {
            addToFavouritesTitleInput.postToSelf {
                showSoftKeyboard()

                moveCursorToTheEnd()
            }
        }
    }
}
