package com.edgeverse.wallet.core_db.converters

import androidx.room.TypeConverter
import com.edgeverse.wallet.core.model.CryptoType

class CryptoTypeConverters {

    @TypeConverter
    fun from(cryptoType: CryptoType): String = cryptoType.name

    @TypeConverter
    fun to(name: String): CryptoType = enumValueOf(name)
}
