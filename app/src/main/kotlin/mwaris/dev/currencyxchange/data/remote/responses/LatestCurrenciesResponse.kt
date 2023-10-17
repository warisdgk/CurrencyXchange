package mwaris.dev.currencyxchange.data.remote.responses

import com.google.gson.annotations.SerializedName
import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.utils.Parselable

data class LatestCurrenciesResponse(
    @SerializedName("timestamp")
    private val timestamp: Long,

    @SerializedName("base")
    private val base: String,

    @SerializedName("rates")
    private val rates: Map<String, Double>,

    ) : Parselable<LatestCurrenciesInfo> {

    override fun parse(): LatestCurrenciesInfo =
        LatestCurrenciesInfo(
            timestamp,
            base,
            rates
        )
}
