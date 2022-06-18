package com.dfinn.wallet.feature_account_impl.presentation.account.advancedEncryption

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import com.dfinn.wallet.common.base.BaseFragment
import com.dfinn.wallet.common.di.FeatureUtils
import com.dfinn.wallet.common.mixin.impl.observeValidations
import com.dfinn.wallet.common.utils.applyStatusBarInsets
import com.dfinn.wallet.common.utils.input.Input
import com.dfinn.wallet.common.utils.onTextChanged
import com.dfinn.wallet.common.utils.setVisible
import com.dfinn.wallet.common.utils.useInputValue
import com.dfinn.wallet.common.view.InputField
import com.dfinn.wallet.common.view.LabeledTextView
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet
import com.dfinn.wallet.feature_account_api.di.AccountFeatureApi
import com.dfinn.wallet.feature_account_impl.R
import com.dfinn.wallet.feature_account_impl.di.AccountFeatureComponent
import com.dfinn.wallet.feature_account_impl.presentation.view.advanced.encryption.EncryptionTypeChooserBottomSheetDialog
import com.dfinn.wallet.feature_account_impl.presentation.view.advanced.encryption.model.CryptoTypeModel
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionApply
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionContainer
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionEthereumCryptoType
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionEthereumDerivationPath
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionSubstrateCryptoType
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionSubstrateDerivationPath
import kotlinx.android.synthetic.main.fragment_advanced_encryption.advancedEncryptionToolbar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdvancedEncryptionFragment : BaseFragment<AdvancedEncryptionViewModel>() {

    companion object {

        private const val PAYLOAD = "CreateAccountFragment.payload"

        fun getBundle(payload: AdvancedEncryptionPayload): Bundle {

            return Bundle().apply {
                putParcelable(PAYLOAD, payload)
            }
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_advanced_encryption, container, false)
    }

    override fun initViews() {
        advancedEncryptionToolbar.setHomeButtonListener { viewModel.homeButtonClicked() }
        advancedEncryptionContainer.applyStatusBarInsets()

        advancedEncryptionApply.setOnClickListener {
            viewModel.applyClicked()
        }

        advancedEncryptionSubstrateCryptoType.setOnClickListener {
            viewModel.substrateEncryptionClicked()
        }
    }

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .advancedEncryptionComponentFactory()
            .create(this, argument(PAYLOAD))
            .inject(this)
    }

    override fun subscribe(viewModel: AdvancedEncryptionViewModel) {
        observeValidations(viewModel)

        advancedEncryptionApply.setVisible(viewModel.applyVisible)

        viewModel.substrateCryptoTypeInput.bindTo(advancedEncryptionSubstrateCryptoType)
        viewModel.substrateDerivationPathInput.bindTo(
            advancedEncryptionSubstrateDerivationPath,
            viewModel::substrateDerivationPathChanged
        )

        viewModel.ethereumCryptoTypeInput.bindTo(advancedEncryptionEthereumCryptoType)
        viewModel.ethereumDerivationPathInput.bindTo(
            advancedEncryptionEthereumDerivationPath,
            viewModel::ethereumDerivationPathChanged
        )

        viewModel.showSubstrateEncryptionTypeChooserEvent.observeEvent {
            showEncryptionChooser(requireContext(), it)
        }
    }

    private fun Flow<Input<String>>.bindTo(view: InputField, onTextChanged: (String) -> Unit) {
        observe { input ->
            with(view) {
                useInputValue(input) {
                    if (content.text.toString() != it) {
                        content.setText(it)
                    }
                }
            }
        }

        view.content.onTextChanged(onTextChanged)
    }

    private fun Flow<Input<CryptoTypeModel>>.bindTo(view: LabeledTextView) {
        observe { input ->
            with(view) {
                useInputValue(input) { setMessage(it.name) }
            }
        }
    }

    private fun showEncryptionChooser(
        context: Context,
        payload: DynamicListBottomSheet.Payload<CryptoTypeModel>,
    ) {
        EncryptionTypeChooserBottomSheetDialog(
            context = context,
            payload = payload,
            onClicked = viewModel::substrateEncryptionChanged
        ).show()
    }
}
