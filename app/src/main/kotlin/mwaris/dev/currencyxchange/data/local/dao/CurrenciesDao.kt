package mwaris.dev.currencyxchange.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mwaris.dev.currencyxchange.data.local.entities.LatestCurrenciesEntity

@Dao
interface CurrenciesDao {
    @Query(value = "SELECT * FROM currenciesInfo")
    fun getCurrencyRatesInfo(): Flow<LatestCurrenciesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrencyRatesInfo(entity: LatestCurrenciesEntity)
}