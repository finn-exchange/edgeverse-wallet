package com.dfinn.wallet.core_db.converters

import androidx.room.TypeConverter
import com.dfinn.wallet.core.model.Node

class NetworkTypeConverters {

    @TypeConverter
    fun fromNetworkType(networkType: Node.NetworkType): Int {
        return networkType.ordinal
    }

    @TypeConverter
    fun toNetworkType(ordinal: Int): Node.NetworkType {
        return Node.NetworkType.values()[ordinal]
    }
}
