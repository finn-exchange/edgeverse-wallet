package com.edgeverse.wallet.feature_dapp_impl.presentation.main.model

data class DAppCategoryModel(
    val id: String,
    val name: String,
    val selected: Boolean
)

class DAppCategoryState(
    val categories: List<DAppCategoryModel>,
    val selectedIndex: Int?
)
