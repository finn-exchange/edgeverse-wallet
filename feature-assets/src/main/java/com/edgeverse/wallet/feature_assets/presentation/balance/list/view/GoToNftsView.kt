package com.edgeverse.wallet.feature_assets.presentation.balance.list.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.presentation.LoadingState
import com.edgeverse.wallet.common.presentation.dataOrNull
import com.edgeverse.wallet.common.utils.WithContextExtensions
import com.edgeverse.wallet.common.utils.dpF
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureApi
import com.edgeverse.wallet.feature_assets.di.AssetsFeatureComponent
import com.edgeverse.wallet.feature_assets.presentation.balance.list.model.NftPreviewUi
import kotlinx.android.synthetic.main.view_go_to_nfts.view.goToNftCounter
import kotlinx.android.synthetic.main.view_go_to_nfts.view.goToNftPreview1
import kotlinx.android.synthetic.main.view_go_to_nfts.view.goToNftPreview2
import kotlinx.android.synthetic.main.view_go_to_nfts.view.goToNftPreview3
import kotlinx.android.synthetic.main.view_go_to_nfts.view.goToNftsShimmer
import javax.inject.Inject

class GoToNftsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions {

    @Inject lateinit var imageLoader: ImageLoader

    override val providedContext: Context = context

    private val previewViews by lazy(LazyThreadSafetyMode.NONE) {
        listOf(goToNftPreview1, goToNftPreview2, goToNftPreview3)
    }

    private val mediaLoadingTransformation = RoundedCornersTransformation(radius = 6.5f.dpF(context))

    init {
        View.inflate(context, R.layout.view_go_to_nfts, this)

        background = addRipple(getRoundedCornerDrawable(R.color.black_48))

        FeatureUtils.getFeature<AssetsFeatureComponent>(
            context,
            AssetsFeatureApi::class.java
        ).inject(this)
    }

    fun setNftCount(countLabel: String?) {
        goToNftCounter.text = countLabel
    }

    fun setPreviews(previews: List<NftPreviewUi>?) {
        val shouldShowLoading = previews == null || previews.any { it is LoadingState.Loading }

        if (shouldShowLoading) {
            previewViews.forEach(View::makeGone)
            goToNftsShimmer.makeVisible()
        } else {
            goToNftsShimmer.makeGone()

            setVisible(previews!!.isNotEmpty())

            previewViews.forEachIndexed { index, view ->
                val previewContent = previews.getOrNull(index)

                if (previewContent == null) { // no such element
                    view.makeGone()
                } else {
                    view.makeVisible()
                    view.load(previewContent.dataOrNull, imageLoader) {
                        transformations(mediaLoadingTransformation)
                    }
                }
            }
        }
    }
}
