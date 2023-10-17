package mwaris.dev.currencyxchange.testdoubles

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.data.repositories.ICurrenciesRepository

class TestCurrenciesRepository : ICurrenciesRepository {

    private val currenciesFlow: MutableSharedFlow<LatestCurrenciesInfo> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getCurrenciesData(): Flow<LatestCurrenciesInfo> = currenciesFlow

    fun setCurrencies(currencies: LatestCurrenciesInfo) {
        currenciesFlow.tryEmit(currencies)
    }

    override suspend fun syncWith() = true
}