package mwaris.dev.currencyxchange.data.remote.datasource

import mwaris.dev.currencyxchange.data.remote.apis.LatestCurrenciesApi
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import javax.inject.Inject

class CurrenciesRemoteDataSource @Inject constructor(
    private val currenciesApi: LatestCurrenciesApi
) : CurrenciesDataSource {
    override suspend fun getCurrenciesData(): LatestCurrenciesInfo {
        return currenciesApi.getUpdatedCurrenciesData().parse()
    }
}