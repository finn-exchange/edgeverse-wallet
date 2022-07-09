package com.edgeverse.wallet.feature_account_impl.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.edgeverse.wallet.common.utils.EventObserver
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.common.utils.nameInputFilters
import com.edgeverse.wallet.common.utils.observe
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.presentation.importing.source.model.JsonImportSource
import kotlinx.android.synthetic.main.import_source_json.view.importJsonContent
import kotlinx.android.synthetic.main.import_source_json.view.importJsonNoNetworkInfo
import kotlinx.android.synthetic.main.import_source_json.view.importJsonPasswordInput
import kotlinx.android.synthetic.main.import_source_json.view.importJsonUsernameInput

class JsonImportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImportSourceView<JsonImportSource>(R.layout.import_source_json, context, attrs, defStyleAttr) {

    override val nameInputViews: ImportAccountNameViews
        get() = ImportAccountNameViews(
            nameInput = importJsonUsernameInput,
            visibilityDependent = emptyList()
        )

    init {
        importJsonUsernameInput.editText!!.filters = nameInputFilters()
    }

    override fun observeSource(source: JsonImportSource, lifecycleOwner: LifecycleOwner) {
        val scope = lifecycleOwner.lifecycle.coroutineScope

        source.jsonContentFlow.observe(scope, importJsonContent::setMessage)

        importJsonContent.setWholeClickListener { source.jsonClicked() }

        source.showJsonInputOptionsEvent.observe(
            lifecycleOwner,
            EventObserver {
                showJsonInputOptionsSheet(source)
            }
        )

        importJsonPasswordInput.content.bindTo(source.passwordFlow, scope)

        importJsonContent.setOnClickListener {
            source.jsonClicked()
        }

        source.showNetworkWarningFlow.observe(scope) {
            importJsonNoNetworkInfo.setVisible(it)
        }
    }

    private fun showJsonInputOptionsSheet(source: JsonImportSource) {
        JsonPasteOptionsSheet(context, source::pasteClicked, source::chooseFileClicked)
            .show()
    }
}
