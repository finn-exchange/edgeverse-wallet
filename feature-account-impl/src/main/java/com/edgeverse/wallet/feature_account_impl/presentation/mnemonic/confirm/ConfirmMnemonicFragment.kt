package com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.edgeverse.wallet.common.base.BaseFragment
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_account_api.di.AccountFeatureApi
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.di.AccountFeatureComponent
import kotlinx.android.synthetic.main.fragment_confirm_mnemonic.confirmMnemonicDestination
import kotlinx.android.synthetic.main.fragment_confirm_mnemonic.confirmMnemonicSource
import kotlinx.android.synthetic.main.fragment_confirm_mnemonic.confirmMnemonicToolbar
import kotlinx.android.synthetic.main.fragment_confirm_mnemonic.conformMnemonicContinue
import kotlinx.android.synthetic.main.fragment_confirm_mnemonic.conformMnemonicSkip

class ConfirmMnemonicFragment : BaseFragment<ConfirmMnemonicViewModel>() {

    companion object {
        private const val KEY_PAYLOAD = "confirm_payload"

        fun getBundle(payload: ConfirmMnemonicPayload): Bundle {

            return Bundle().apply {
                putParcelable(KEY_PAYLOAD, payload)
            }
        }
    }

    private val sourceAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ConfirmMnemonicAdapter(itemHandler = viewModel::sourceWordClicked)
    }

    private val destinationAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ConfirmMnemonicAdapter(itemHandler = viewModel::destinationWordClicked)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_mnemonic, container, false)
    }

    override fun initViews() {
        confirmMnemonicToolbar.setHomeButtonListener { viewModel.homeButtonClicked() }
        confirmMnemonicToolbar.setRightActionClickListener { viewModel.reset() }

        conformMnemonicSkip.setOnClickListener { viewModel.skipClicked() }
        conformMnemonicContinue.setOnClickListener { viewModel.continueClicked() }

        confirmMnemonicSource.adapter = sourceAdapter
        confirmMnemonicDestination.adapter = destinationAdapter
    }

    override fun inject() {
        val payload = argument<ConfirmMnemonicPayload>(KEY_PAYLOAD)

        FeatureUtils.getFeature<AccountFeatureComponent>(context!!, AccountFeatureApi::class.java)
            .confirmMnemonicComponentFactory()
            .create(this, payload)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmMnemonicViewModel) {
        conformMnemonicSkip.setVisible(viewModel.skipVisible)

        viewModel.sourceWords.observe { sourceAdapter.submitList(it) }
        viewModel.destinationWords.observe { destinationAdapter.submitList(it) }

        viewModel.nextButtonEnabled.observe {
            conformMnemonicContinue.isEnabled = it
        }

        viewModel.matchingMnemonicErrorAnimationEvent.observeEvent {
            playMatchingMnemonicErrorAnimation()
        }
    }

    private fun playMatchingMnemonicErrorAnimation() {
        val animation = AnimationUtils.loadAnimation(activity!!, R.anim.shake)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                viewModel.matchingErrorAnimationCompleted()
            }
        })

        confirmMnemonicDestination.startAnimation(animation)
    }
}
