package com.edgeverse.wallet.common.view.shape

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.edgeverse.wallet.common.R
import com.edgeverse.wallet.common.utils.getAccentColor
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

fun Int.toColorStateList() = ColorStateList.valueOf(this)

fun Context.addRipple(
    drawable: Drawable? = null,
    mask: Drawable? = null,
    @ColorInt rippleColor: Int = getColor(R.color.colorSelected)
): Drawable {

    return RippleDrawable(rippleColor.toColorStateList(), drawable, mask)
}

fun Context.getCutCornersStateDrawable(
    disabledDrawable: Drawable = getDisabledDrawable(),
    focusedDrawable: Drawable = getFocusedDrawable(),
    idleDrawable: Drawable = getIdleDrawable()
): Drawable {
    return StateListDrawable().apply {
        addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
        addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
        addState(StateSet.WILD_CARD, idleDrawable)
    }
}

fun Context.getCornersStateDrawable(
    disabledDrawable: Drawable = getDisabledDrawable(),
    focusedDrawable: Drawable = getFocusedDrawable(),
    idleDrawable: Drawable = getIdleDrawable(),
): Drawable {
    return StateListDrawable().apply {
        addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
        addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
        addState(StateSet.WILD_CARD, idleDrawable)
    }
}

fun Context.getInputBackground() = getCornersStateDrawable(
    focusedDrawable = getRoundedCornerDrawableFromColors(
        fillColor = getColor(R.color.white_8),
        strokeColor = getAccentColor(),
        strokeSizeInDp = 1f
    ),
    idleDrawable = getRoundedCornerDrawable(R.color.white_8)
)

fun Context.getFocusedDrawable(): Drawable = getCutCornerDrawable(strokeColorRes = R.color.white)
fun Context.getDisabledDrawable(): Drawable = getCutCornerDrawable(fillColorRes = R.color.gray3)
fun Context.getIdleDrawable(): Drawable = getCutCornerDrawable(strokeColorRes = R.color.gray2)
fun Context.getSelectedDrawable(): Drawable = getCutCornerDrawable(strokeColorRes = R.color.colorAccent)
fun Context.getBlurDrawable(@ColorRes strokeColorRes: Int? = null): Drawable {
    return getRoundedCornerDrawable(fillColorRes = R.color.black_48, strokeColorRes = strokeColorRes)
}

fun Context.getCutCornerDrawable(
    @ColorRes fillColorRes: Int = R.color.black,
    @ColorRes strokeColorRes: Int? = null,
    cornerSizeInDp: Int = 10,
    strokeSizeInDp: Int = 1
): Drawable {
    val fillColor = getColor(fillColorRes)
    val strokeColor = strokeColorRes?.let(this::getColor)

    return getCutCornerDrawableFromColors(fillColor, strokeColor, cornerSizeInDp, strokeSizeInDp)
}

fun Context.getCutCornerDrawableFromColors(
    @ColorInt fillColor: Int = getColor(R.color.black),
    @ColorInt strokeColor: Int? = null,
    cornerSizeInDp: Int = 10,
    strokeSizeInDp: Int = 1
): Drawable {
    val density = resources.displayMetrics.density

    val cornerSizePx = density * cornerSizeInDp
    val strokeSizePx = density * strokeSizeInDp

    return ShapeDrawable(CutCornersShape(cornerSizePx, strokeSizePx, fillColor, strokeColor))
}

fun Context.getCutLeftBottomCornerDrawableFromColors(
    @ColorInt fillColor: Int = getColor(R.color.colorAccent_50),
    cornerSizeXInDp: Int = 10,
    cornerSizeYInDp: Int = 8
): Drawable {
    val density = resources.displayMetrics.density

    val cornerSizeXPx = density * cornerSizeXInDp
    val cornerSizeYPx = density * cornerSizeYInDp

    return ShapeDrawable(CutLeftBottomCornerShape(cornerSizeXPx, cornerSizeYPx, fillColor))
}

fun Context.getRoundedCornerDrawable(
    @ColorRes fillColorRes: Int = R.color.black,
    @ColorRes strokeColorRes: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    val fillColor = getColor(fillColorRes)
    val strokeColor = strokeColorRes?.let(this::getColor)

    return getRoundedCornerDrawableFromColors(fillColor, strokeColor, cornerSizeInDp, strokeSizeInDp)
}

fun Context.getTopRoundedCornerDrawable(
    @ColorRes fillColorRes: Int = R.color.black,
    @ColorRes strokeColorRes: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    val fillColor = getColor(fillColorRes)
    val strokeColor = strokeColorRes?.let(this::getColor)

    return getTopRoundedCornerDrawableFromColors(fillColor, strokeColor, cornerSizeInDp, strokeSizeInDp)
}

@SuppressLint("RestrictedApi")
fun Context.getTopRoundedCornerDrawableFromColors(
    @ColorInt fillColor: Int = getColor(R.color.black),
    @ColorInt strokeColor: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    return cornerDrawableFromColors(
        fillColor = fillColor,
        strokeColor = strokeColor,
        cornerSizeInDp = cornerSizeInDp,
        strokeSizeInDp = strokeSizeInDp,
        shapeBuilder = { cornerSizePx ->
            ShapeAppearanceModel.Builder()
                .setTopLeftCorner(ROUNDED, cornerSizePx)
                .setTopRightCorner(ROUNDED, cornerSizePx)
                .build()
        }
    )
}

@SuppressLint("RestrictedApi")
fun Context.getRoundedCornerDrawableFromColors(
    @ColorInt fillColor: Int = getColor(R.color.black),
    @ColorInt strokeColor: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    return cornerDrawableFromColors(
        fillColor = fillColor,
        strokeColor = strokeColor,
        cornerSizeInDp = cornerSizeInDp,
        strokeSizeInDp = strokeSizeInDp,
        shapeBuilder = { cornerSizePx ->
            ShapeAppearanceModel.Builder()
                .setAllCorners(ROUNDED, cornerSizePx)
                .build()
        }
    )
}

private fun Context.cornerDrawableFromColors(
    @ColorInt fillColor: Int,
    @ColorInt strokeColor: Int?,
    cornerSizeInDp: Int,
    strokeSizeInDp: Float,
    shapeBuilder: (cornerSize: Float) -> ShapeAppearanceModel
): Drawable {
    val density = resources.displayMetrics.density

    val cornerSizePx = density * cornerSizeInDp
    val strokeSizePx = density * strokeSizeInDp

    return MaterialShapeDrawable(shapeBuilder(cornerSizePx)).apply {
        setFillColor(ColorStateList.valueOf(fillColor))

        strokeColor?.let {
            setStroke(strokeSizePx, it)
        }
    }
}
