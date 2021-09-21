package jp.co.soramitsu.core_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.soramitsu.core_db.model.TokenLocal
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TokenDao {

    @Query("SELECT EXISTS(SELECT * FROM tokens WHERE symbol = :symbol)")
    abstract suspend fun isTokenExists(symbol: String): Boolean

    @Query("select * from tokens where symbol = :symbol")
    abstract suspend fun getToken(symbol: String): TokenLocal?

    @Query("select * from tokens where symbol = :symbol")
    abstract fun observeToken(symbol: String): Flow<TokenLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertToken(token: TokenLocal)
}
