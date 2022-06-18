package com.dfinn.wallet.common.data.network.coingecko

import com.dfinn.wallet.common.utils.Event
import com.dfinn.wallet.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet

typealias FiatChooserEvent = Event<DynamicListBottomSheet.Payload<FiatCurrency>>

data class FiatCurrency(val id: String, val symbol: String, val name: String, val icon: String)
