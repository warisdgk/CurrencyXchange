package mwaris.dev.currencyxchange.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo

@Entity(
    tableName = "currenciesInfo",
)
data class LatestCurrenciesEntity(
    @PrimaryKey
    val baseCurrency: String,
    @ColumnInfo
    val timestamp: Long,
    @ColumnInfo
    val rates: Map<String, Double>,
)

fun LatestCurrenciesEntity.asExternalModel() = LatestCurrenciesInfo(
    baseCurrency = baseCurrency,
    timestamp = timestamp,
    rates = rates,
)