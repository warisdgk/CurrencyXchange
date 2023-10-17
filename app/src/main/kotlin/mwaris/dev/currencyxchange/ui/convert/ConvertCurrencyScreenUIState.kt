package mwaris.dev.currencyxchange.ui.convert

sealed interface ConvertCurrencyScreenUIState {
    object Loading : ConvertCurrencyScreenUIState

    data class Success(
        val convertedCurrenciesInfo: List<ConvertedCurrencyItem>,
    ) : ConvertCurrencyScreenUIState
}

data class ConvertedCurrencyItem(
    val currencyCode: String,
    val currencyRate: Double
)