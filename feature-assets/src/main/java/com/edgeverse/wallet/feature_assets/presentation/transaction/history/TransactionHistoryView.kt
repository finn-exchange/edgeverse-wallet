package com.edgeverse.wallet.feature_assets.presentation.transaction.history

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.edgeverse.wallet.common.di.FeatureUtils
import com.edgeverse.wallet.common.utils.dp
import com.edgeverse.wallet.common.utils.enableShowingNewlyAddedTopElements
import com.edgeverse.wallet.common.utils.makeGone
import com.edgeverse.wallet.common.utils.makeVisible
import com.edgeverse.wallet.common.utils.setVisible
import com.edgeverse.wallet.common.utils.updateTopMargin
import com.edgeverse.wallet.common.view.bottomSheet.LockBottomSheetBehavior
import com.edgeverse.wallet.common.view.shape.getTopRoundedCornerDrawable
import com.edgeverse.wallet.feature_assets.R
import com.edgeverse.wallet.feature_assets.presentation.model.OperationModel
import kotlinx.android.synthetic.main.view_transfer_history.view.placeholder
import kotlinx.android.synthetic.main.view_transfer_history.view.transactionHistoryFilter
import kotlinx.android.synthetic.main.view_transfer_history.view.transactionHistoryList
import kotlinx.android.synthetic.main.view_transfer_history.view.transactionHistoryProgress
import kotlinx.android.synthetic.main.view_transfer_history.view.transactionHistoryPuller
import kotlinx.android.synthetic.main.view_transfer_history.view.transactionHistoryTitle

typealias ScrollingListener = (position: Int) -> Unit
typealias SlidingStateListener = (Int) -> Unit
typealias TransactionClickListener = (OperationModel) -> Unit

private const val MIN_ALPHA = (0.48 * 255).toInt()
private const val MAX_ALPHA = 1 * 255

private const val MIN_MARGIN = 20 // dp
private const val MAX_MARGIN = 32 // dp

private const val PULLER_VISIBILITY_OFFSET = 0.9

private const val OFFSET_KEY = "OFFSET"
private const val SUPER_STATE = "SUPER_STATE"

class TransferHistorySheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), TransactionHistoryAdapter.Handler {

    private var bottomSheetBehavior: LockBottomSheetBehavior<View>? = null

    private var anchor: View? = null

    private var scrollingListener: ScrollingListener? = null
    private var slidingStateListener: SlidingStateListener? = null
    private var transactionClickListener: TransactionClickListener? = null

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        FeatureUtils.getCommonApi(context).imageLoader()
    }

    private val adapter = TransactionHistoryAdapter(this, imageLoader)

    private var lastOffset: Float = 0.0F

    private var adapterDataObserver: RecyclerView.AdapterDataObserver? = null

    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        anchor?.let {
            bottomSheetBehavior?.peekHeight = parentView.measuredHeight - it.bottom
        }
    }

    init {
        View.inflate(context, R.layout.view_transfer_history, this)

        background = context.getTopRoundedCornerDrawable(fillColorRes = R.color.black)

        transactionHistoryList.adapter = adapter
        transactionHistoryList.setHasFixedSize(true)

        addScrollListener()

        updateSlidingEffects()
    }

    fun setFiltersVisible(visible: Boolean) {
        transactionHistoryFilter.setVisible(visible)
    }

    fun showProgress() {
        placeholder.makeGone()
        transactionHistoryProgress.makeVisible()
        transactionHistoryList.makeGone()

        adapter.submitList(emptyList())

        bottomSheetBehavior?.isDraggable = false
    }

    fun showPlaceholder() {
        placeholder.makeVisible()
        transactionHistoryProgress.makeGone()
        transactionHistoryList.makeGone()

        adapter.submitList(emptyList())

        bottomSheetBehavior?.isDraggable = false
    }

    fun showTransactions(transactions: List<Any>) {
        placeholder.makeGone()
        transactionHistoryProgress.makeGone()
        transactionHistoryList.makeVisible()

        bottomSheetBehavior?.isDraggable = true

        adapter.submitList(transactions)
    }

    fun setScrollingListener(listener: ScrollingListener) {
        scrollingListener = listener
    }

    fun setSlidingStateListener(listener: SlidingStateListener) {
        slidingStateListener = listener
    }

    fun setTransactionClickListener(listener: TransactionClickListener) {
        transactionClickListener = listener
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        return Bundle().apply {
            putParcelable(SUPER_STATE, superState)
            putFloat(OFFSET_KEY, lastOffset)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state[SUPER_STATE] as Parcelable)

            lastOffset = state.getFloat(OFFSET_KEY)
            updateSlidingEffects()
        }

        bottomSheetBehavior?.state?.let {
            slidingStateListener?.invoke(it)
        }
    }

    fun setFilterClickListener(clickListener: OnClickListener) {
        transactionHistoryFilter.setOnClickListener(clickListener)
    }

    fun initializeBehavior(anchorView: View) {
        anchor = anchorView

        bottomSheetBehavior = LockBottomSheetBehavior.fromView(this)

        bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                lastOffset = slideOffset

                updateSlidingEffects()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                slidingStateListener?.invoke(newState)
            }
        })

        addLayoutListener()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        adapterDataObserver = transactionHistoryList.enableShowingNewlyAddedTopElements()
    }

    override fun onDetachedFromWindow() {
        removeLayoutListener()

        adapter.unregisterAdapterDataObserver(adapterDataObserver!!)

        super.onDetachedFromWindow()
    }

    override fun transactionClicked(transactionModel: OperationModel) {
        transactionClickListener?.invoke(transactionModel)
    }

    private fun addScrollListener() {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                scrollingListener?.invoke(lastVisiblePosition)
            }
        }

        transactionHistoryList.addOnScrollListener(scrollListener)
    }

    private fun removeLayoutListener() {
        parentView.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    private fun addLayoutListener() {
        parentView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

    private fun updateSlidingEffects() {
        updateBackgroundAlpha()

        updateTitleMargin()

        updatePullerVisibility()

        requestLayout()
    }

    private fun updateTitleMargin() {
        val newMargin = linearUpdate(MIN_MARGIN, MAX_MARGIN, lastOffset)
        val newMarginPx = newMargin.dp(context)

        transactionHistoryTitle.updateTopMargin(newMarginPx)
    }

    private fun updatePullerVisibility() {
        transactionHistoryPuller.setVisible(lastOffset < PULLER_VISIBILITY_OFFSET, falseState = View.INVISIBLE)
    }

    private fun updateBackgroundAlpha() {
        val updatedAlpha = linearUpdate(MIN_ALPHA, MAX_ALPHA, lastOffset)

        val color = Color.argb(updatedAlpha, 0, 0, 0)

        backgroundTintList = ColorStateList.valueOf(color)
    }

    private val parentView: View
        get() = parent as View

    private fun linearUpdate(min: Int, max: Int, progress: Float): Int {
        return (min + (max - min) * progress).toInt()
    }
}
