package mwaris.dev.currencyxchange.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import mwaris.dev.currencyxchange.data.local.dao.CurrenciesDao
import mwaris.dev.currencyxchange.data.local.entities.LatestCurrenciesEntity

class TestCurrenciesDao : CurrenciesDao {

    private var entitiesStateFlow = MutableStateFlow(
        LatestCurrenciesEntity(
            "",
            0,
            mapOf()
        )
    )

    override fun getCurrencyRatesInfo(): Flow<LatestCurrenciesEntity> = entitiesStateFlow

    override suspend fun saveCurrencyRatesInfo(entity: LatestCurrenciesEntity) {
        entitiesStateFlow.update { oldValues ->
            oldValues.copy(
                baseCurrency = entity.baseCurrency,
                timestamp = entity.timestamp,
                rates = entity.rates
            )
        }
    }
}