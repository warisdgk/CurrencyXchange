package mwaris.dev.currencyxchange.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mwaris.dev.currencyxchange.data.local.dao.CurrenciesDao
import mwaris.dev.currencyxchange.data.local.entities.LatestCurrenciesEntity
import mwaris.dev.currencyxchange.data.local.typeconverters.MapTypeConverter

@Database(
    entities = [LatestCurrenciesEntity::class], version = 1,
)
@TypeConverters(MapTypeConverter::class)
abstract class XchangeDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
}