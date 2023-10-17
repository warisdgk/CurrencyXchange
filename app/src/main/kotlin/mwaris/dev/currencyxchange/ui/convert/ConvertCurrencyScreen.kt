package mwaris.dev.currencyxchange.ui.convert

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mwaris.dev.currencyxchange.R
import mwaris.dev.currencyxchange.ui.theme.CurrencyXchangeTheme
import mwaris.dev.currencyxchange.utils.ONLY_NUMBERS
import mwaris.dev.currencyxchange.utils.formatCurrencyRate

@Composable
fun CurrencyConversionScreen(
    viewModel: ConvertCurrencyViewModel = hiltViewModel(),
) {
    val convertedCurrencies by viewModel.convertedCurrenciesInfo.collectAsStateWithLifecycle()

    when (convertedCurrencies) {
        ConvertCurrencyScreenUIState.Loading -> {
            ShowProgress()
        }

        is ConvertCurrencyScreenUIState.Success -> {
            ShowConvertedCurrencies(viewModel, convertedCurrencies)
        }
    }
}

@Composable
private fun ShowProgress() {
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.progress_bar_width)),
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
private fun ShowConvertedCurrencies(
    viewModel: ConvertCurrencyViewModel,
    convertedCurrencies: ConvertCurrencyScreenUIState
) {
    val userSelections by viewModel.userSelections.collectAsStateWithLifecycle()
    val listOfAvailableCurrencies by viewModel.listOfAvailableCurrencies.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        EnterAmountTextField {
            viewModel.updateAmountToConvert(it)
        }
        CurrencyDropdown(
            userSelections.currencyCode,
            listOfAvailableCurrencies = listOfAvailableCurrencies
        ) {
            viewModel.updateSelectedCurrencyCode(it)
        }
        CurrenciesLazyGrid(convertedCurrencies)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EnterAmountTextField(
    onAmountEntered: (String) -> Unit,
) {
    var amountToConvert by remember {
        mutableStateOf("")
    }
    val pattern = remember { Regex(ONLY_NUMBERS) }

    OutlinedTextField(
        value = amountToConvert,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        onValueChange = {
            if (it.isEmpty() || it.matches(pattern)) {
                amountToConvert = it
                onAmountEntered.invoke(it)
            }
        },
        label = { Text(stringResource(R.string.add_amount_to_convert)) },
        modifier = Modifier.padding(dimensionResource(id = R.dimen.default_spacing))
    )
}

@Composable
private fun CurrencyDropdown(
    initialState: String,
    listOfAvailableCurrencies: List<String>,
    onCurrencyCodeSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrencyCode by remember { mutableStateOf(initialState) }

    Box(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.default_spacing)),
    ) {
        Button(
            onClick = { expanded = !expanded }) {
            Text(selectedCurrencyCode)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            listOfAvailableCurrencies.forEach { label ->
                DropdownMenuItem(
                    text = {
                        Text(text = label)
                    }, onClick = {
                        selectedCurrencyCode = label
                        onCurrencyCodeSelected.invoke(label)
                        expanded = false
                    })
            }
        }

    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun CurrenciesLazyGrid(convertedCurrenciesInfo: ConvertCurrencyScreenUIState) {
    if (convertedCurrenciesInfo is ConvertCurrencyScreenUIState.Success) {
        if (convertedCurrenciesInfo
                .convertedCurrenciesInfo.isNotEmpty()
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = dimensionResource(id = R.dimen.small_spacing),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_spacing)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.small_spacing)),
                content = {
                    items(
                        convertedCurrenciesInfo
                            .convertedCurrenciesInfo,
                    ) { item ->
                        ElevatedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Row(horizontalArrangement = Arrangement.Center) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = dimensionResource(id = R.dimen.small_spacing)
                                    ),
                                    text = "${item.currencyCode} ${formatCurrencyRate(item.currencyRate)}"
                                )
                            }
                        }
                    }
                },
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyXchangeTheme {
        CurrencyConversionScreen()
    }
}