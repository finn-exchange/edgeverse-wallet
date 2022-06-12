package io.novafoundation.nova.common.data.network.coingecko

import io.novafoundation.nova.common.utils.Event
import io.novafoundation.nova.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet

typealias FiatChooserEvent = Event<DynamicListBottomSheet.Payload<FiatCurrency>>

data class FiatCurrency(val id: String, val symbol: String, val name: String, val icon: String)
