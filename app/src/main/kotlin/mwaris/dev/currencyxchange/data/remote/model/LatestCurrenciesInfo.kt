package mwaris.dev.currencyxchange.data.remote.model

import mwaris.dev.currencyxchange.data.local.entities.LatestCurrenciesEntity

data class LatestCurrenciesInfo(
    val timestamp: Long,
    val baseCurrency: String,
    val rates: Map<String,Double>
)

fun LatestCurrenciesInfo.asEntity() = LatestCurrenciesEntity(
    timestamp = timestamp,
    baseCurrency = baseCurrency,
    rates = rates
)