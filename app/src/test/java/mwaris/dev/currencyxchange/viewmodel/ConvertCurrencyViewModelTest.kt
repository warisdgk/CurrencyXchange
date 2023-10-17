package mwaris.dev.currencyxchange.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.testdoubles.TestCurrenciesRepository
import mwaris.dev.currencyxchange.ui.convert.ConvertCurrencyScreenUIState
import mwaris.dev.currencyxchange.ui.convert.ConvertCurrencyViewModel
import mwaris.dev.currencyxchange.ui.convert.ConvertedCurrencyItem
import mwaris.dev.currencyxchange.ui.convert.UserSelection
import mwaris.dev.currencyxchange.ui.convert.defaultSelection
import mwaris.dev.currencyxchange.utils.MainDispatcherRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class ConvertCurrencyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testCurrenciesRepository = TestCurrenciesRepository()
    private lateinit var viewModel: ConvertCurrencyViewModel

    @Before
    fun setup() {
        viewModel = ConvertCurrencyViewModel(
            testCurrenciesRepository,
            mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun validateInitialUserSelection() = runTest {
        Assert.assertEquals(
            UserSelection(),
            viewModel.userSelections.value,
        )
    }

    @Test
    fun validateInitialAvailableCountries() = runTest {
        Assert.assertEquals(
            emptyList<String>(),
            viewModel.listOfAvailableCurrencies.value,
        )
    }

    @Test
    fun validateUserSelectionWithCurrencyCode() = runTest {

        val collectUserSelections =
            launch(UnconfinedTestDispatcher()) {
                viewModel.userSelections.collect()
            }

        Assert.assertEquals(
            defaultSelection,
            viewModel.userSelections.value.currencyCode,
        )

        viewModel.updateSelectedCurrencyCode("AED")
        Assert.assertEquals(
            "AED",
            viewModel.userSelections.value.currencyCode,
        )

        collectUserSelections.cancel()
    }

    @Test
    fun validateIsLoadingShownWhenCurrencyRateAreNotAvailable() = runTest {
        testCurrenciesRepository.setCurrencies(
            LatestCurrenciesInfo(
                0,
                "",
                emptyMap()
            )
        )
        Assert.assertEquals(
            ConvertCurrencyScreenUIState.Loading,
            viewModel.convertedCurrenciesInfo.value,
        )
    }

    @Test
    fun validateCorrectCurrencyConversionsBasedOnUserSelections() = runTest {

        val collectUserSelections =
            launch(UnconfinedTestDispatcher()) {
                viewModel.userSelections.collect()
            }

        val collectAvailableCurrencies =
            launch(UnconfinedTestDispatcher()) {
                viewModel.listOfAvailableCurrencies.collect()
            }

        val collectConvertedCurrencies =
            launch(UnconfinedTestDispatcher()) {
                viewModel.convertedCurrenciesInfo.collect()
            }

        testCurrenciesRepository.setCurrencies(
            sampleCurrenciesData
        )

        viewModel.updateAmountToConvert("20")
        viewModel.updateSelectedCurrencyCode("PKR")

        Assert.assertEquals(
            ConvertCurrencyScreenUIState.Success(
                convertedCurrenciesInfo = listOf(
                    ConvertedCurrencyItem(
                        "AFN",
                        79.54499999999999
                    ),
                    ConvertedCurrencyItem(
                        "ALL",
                        89.65213454545456
                    ),
                    ConvertedCurrencyItem(
                        "PKR",
                        20.0
                    ),
                    ConvertedCurrencyItem(
                        "AED",
                        3.3387727272727274
                    ),
                )
            ),
            viewModel.convertedCurrenciesInfo.value,
        )

        collectUserSelections.cancel()
        collectAvailableCurrencies.cancel()
        collectConvertedCurrencies.cancel()

    }

}

val sampleCurrenciesData = LatestCurrenciesInfo(
    timestamp = Calendar.getInstance().timeInMillis,
    baseCurrency = "USD",
    rates = mapOf(
        "AFN" to 87.4995,
        "ALL" to 98.617348,
        "PKR" to 22.0,
        "AED" to 3.67265,
    )
)