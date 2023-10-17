package mwaris.dev.currencyxchange.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mwaris.dev.currencyxchange.data.local.entities.LatestCurrenciesEntity
import mwaris.dev.currencyxchange.data.local.entities.asExternalModel
import mwaris.dev.currencyxchange.data.remote.model.asEntity
import mwaris.dev.currencyxchange.data.repositories.CurrenciesRepository
import mwaris.dev.currencyxchange.testdoubles.TestCurrenciesDao
import mwaris.dev.currencyxchange.testdoubles.TestCurrenciesNetworkDataSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrenciesRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var testCurrenciesDao: TestCurrenciesDao
    private lateinit var testCurrenciesNetworkDataSource: TestCurrenciesNetworkDataSource

    private lateinit var currenciesRepository: CurrenciesRepository

    @Before
    fun setup() {
        testCurrenciesDao = TestCurrenciesDao()
        testCurrenciesNetworkDataSource = TestCurrenciesNetworkDataSource()

        currenciesRepository = CurrenciesRepository(
            currenciesDao = testCurrenciesDao,
            dataSource = testCurrenciesNetworkDataSource
        )
    }

    @Test
    fun repositoryItemsAreServedByCurrenciesDao() =
        testScope.runTest {
            assertEquals(
                testCurrenciesDao.getCurrencyRatesInfo()
                    .map(LatestCurrenciesEntity::asExternalModel).first(),
                currenciesRepository.getCurrenciesData()
                    .first(),
            )
        }

    @Test
    fun repositorySaveItemsFromNetworkToDB() =
        testScope.runTest {
            currenciesRepository.syncWith()
            val networkCurrencies = testCurrenciesNetworkDataSource.getCurrenciesData().asEntity()

            testCurrenciesDao.saveCurrencyRatesInfo(networkCurrencies)

            val dbCurrencies = testCurrenciesDao.getCurrencyRatesInfo()
                .first()

            assertEquals(
                networkCurrencies,
                dbCurrencies,
            )
        }

}