package mwaris.dev.currencyxchange.data.remote.datasource

import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo

interface CurrenciesDataSource {
    suspend fun getCurrenciesData(): LatestCurrenciesInfo
}