package com.dfinn.wallet.feature_wallet_api.presentation.view

import android.content.Context
import android.util.AttributeSet
import com.dfinn.wallet.common.utils.makeGone
import com.dfinn.wallet.common.utils.useAttributes
import com.dfinn.wallet.common.view.section.SectionView
import com.dfinn.wallet.feature_wallet_api.R
import com.dfinn.wallet.feature_wallet_api.presentation.model.AmountModel
import kotlinx.android.synthetic.main.section_price.view.sectionPriceFiat
import kotlinx.android.synthetic.main.section_price.view.sectionPriceToken
import kotlinx.android.synthetic.main.section_price.view.sectionTitle

class PriceSectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : SectionView(R.layout.section_price, context, attrs, defStyleAttr) {

    init {
        attrs?.let(::applyAttrs)
    }

    fun setPrice(amountModel: AmountModel) {
        sectionPriceToken.text = amountModel.token
        sectionPriceFiat.text = amountModel.fiat
    }

    fun setTitle(title: String) {
        sectionTitle.text = title
    }

    private fun applyAttrs(attrs: AttributeSet) = context.useAttributes(attrs, R.styleable.PriceSectionView) {
        val title = it.getString(R.styleable.PriceSectionView_sectionTitle)
        title?.let(::setTitle)
    }
}

fun PriceSectionView.setPriceOrHide(amountModel: AmountModel?) = if (amountModel != null) {
    setPrice(amountModel)
} else {
    makeGone()
}
