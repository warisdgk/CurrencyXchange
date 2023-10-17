package mwaris.dev.currencyxchange.ui.convert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.data.repositories.ICurrenciesRepository
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    currencyRepository: ICurrenciesRepository,
) : ViewModel() {
    private val _userSelection = MutableStateFlow(UserSelection())
    val userSelections: StateFlow<UserSelection> =
        _userSelection
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserSelection(),
            )


    val listOfAvailableCurrencies: StateFlow<List<String>> =
        currencyRepository.getCurrenciesData()
            .map {
                it.rates.keys.toList()
            }.flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )


    val convertedCurrenciesInfo: StateFlow<ConvertCurrencyScreenUIState> =
        combine(
            userSelections,
            currencyRepository.getCurrenciesData(),
        ) { userSelections, latestCurrencyRates ->
            if (latestCurrencyRates.rates.isEmpty()) {
                ConvertCurrencyScreenUIState.Loading
            } else {
                if (userSelections.amountToConvert.isEmpty() ||
                    userSelections.currencyCode == defaultSelection
                ) {
                    ConvertCurrencyScreenUIState.Success(emptyList())
                } else {
                    val currenciesItems =
                        convertCurrencies(userSelections, latestCurrencyRates)
                    ConvertCurrencyScreenUIState.Success(convertedCurrenciesInfo = currenciesItems)
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ConvertCurrencyScreenUIState.Loading,
            )

    private suspend fun convertCurrencies(
        userSelections: UserSelection,
        latestCurrencyRates: LatestCurrenciesInfo
    ): List<ConvertedCurrencyItem> =
        withContext(Dispatchers.Default) {
            val amountToConvert = userSelections.amountToConvert.toDouble()
            val baseCurrencyRate: Double =
                latestCurrencyRates.rates.getOrDefault(userSelections.currencyCode, 0.0)
            val currenciesItems = latestCurrencyRates.rates.map {
                val baseOffset = it.value / baseCurrencyRate
                ConvertedCurrencyItem(
                    it.key,
                    (amountToConvert * baseOffset)
                )
            }
            currenciesItems
        }


    fun updateAmountToConvert(amountToConvert: String) {
        _userSelection.update {
            it.copy(
                amountToConvert = amountToConvert
            )
        }
    }

    fun updateSelectedCurrencyCode(selectedCurrency: String) {
        _userSelection.update {
            it.copy(
                currencyCode = selectedCurrency
            )
        }
    }
}

const val defaultSelection = "Select Currency"

data class UserSelection(
    val currencyCode: String = defaultSelection,
    val amountToConvert: String = ""
)
