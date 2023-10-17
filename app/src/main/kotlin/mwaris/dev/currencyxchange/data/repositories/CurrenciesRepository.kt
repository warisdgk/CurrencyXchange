package mwaris.dev.currencyxchange.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import mwaris.dev.currencyxchange.data.local.entities.LatestCurrenciesEntity
import mwaris.dev.currencyxchange.data.local.entities.asExternalModel
import mwaris.dev.currencyxchange.data.local.dao.CurrenciesDao
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.data.remote.datasource.CurrenciesDataSource
import mwaris.dev.currencyxchange.data.remote.model.asEntity
import mwaris.dev.currencyxchange.data.sync.helpers.Synchronizer
import javax.inject.Inject

class CurrenciesRepository @Inject constructor(
    private val currenciesDao: CurrenciesDao,
    private val dataSource: CurrenciesDataSource,
) : ICurrenciesRepository {
    override fun getCurrenciesData(): Flow<LatestCurrenciesInfo> =
        currenciesDao.getCurrencyRatesInfo()
            .filterNotNull()
            .map(LatestCurrenciesEntity::asExternalModel)

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return kotlin.runCatching {
            val remoteData = dataSource.getCurrenciesData()
            currenciesDao.saveCurrencyRatesInfo(
                remoteData.asEntity()
            )
        }.isSuccess
    }

}

