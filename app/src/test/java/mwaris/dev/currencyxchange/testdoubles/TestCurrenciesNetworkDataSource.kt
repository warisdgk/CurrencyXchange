package mwaris.dev.currencyxchange.testdoubles

import mwaris.dev.currencyxchange.data.remote.datasource.CurrenciesDataSource
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.viewmodel.sampleCurrenciesData

class TestCurrenciesNetworkDataSource : CurrenciesDataSource {

    override suspend fun getCurrenciesData(): LatestCurrenciesInfo = sampleCurrenciesData

}