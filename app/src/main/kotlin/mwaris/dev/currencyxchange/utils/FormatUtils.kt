package mwaris.dev.currencyxchange.utils

import java.math.RoundingMode
import java.text.DecimalFormat

const val ONLY_NUMBERS = "^\\d+\$"
fun formatCurrencyRate(amountToFormat: Double): String {
    val decimalFormat = DecimalFormat("#.##")
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(amountToFormat)
}