package com.edgeverse.wallet.feature_account_impl.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.edgeverse.wallet.common.utils.bindTo
import com.edgeverse.wallet.common.utils.nameInputFilters
import com.edgeverse.wallet.common.view.shape.getIdleDrawable
import com.edgeverse.wallet.feature_account_impl.R
import com.edgeverse.wallet.feature_account_impl.presentation.importing.source.model.RawSeedImportSource
import kotlinx.android.synthetic.main.import_source_seed.view.importSeedContent
import kotlinx.android.synthetic.main.import_source_seed.view.importSeedContentContainer
import kotlinx.android.synthetic.main.import_source_seed.view.importSeedUsernameHint
import kotlinx.android.synthetic.main.import_source_seed.view.importSeedUsernameInput

class SeedImportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImportSourceView<RawSeedImportSource>(R.layout.import_source_seed, context, attrs, defStyleAttr) {

    override val nameInputViews: ImportAccountNameViews
        get() = ImportAccountNameViews(
            nameInput = importSeedUsernameInput,
            visibilityDependent = listOf(importSeedUsernameHint)
        )

    init {
        importSeedContentContainer.background = context.getIdleDrawable()

        importSeedUsernameInput.content.filters = nameInputFilters()
    }

    override fun observeSource(source: RawSeedImportSource, lifecycleOwner: LifecycleOwner) {
        importSeedContent.bindTo(source.rawSeedFlow, lifecycleOwner.lifecycle.coroutineScope)
    }
}
