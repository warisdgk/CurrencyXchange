package mwaris.dev.currencyxchange.data.repositories

import kotlinx.coroutines.flow.Flow
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.data.sync.helpers.Syncable
import mwaris.dev.currencyxchange.ui.convert.ConvertedCurrencyItem

interface ICurrenciesRepository : Syncable {
    fun getCurrenciesData(): Flow<LatestCurrenciesInfo>
}