package mwaris.dev.currencyxchange.data.remote.apis

import mwaris.dev.currencyxchange.BuildConfig
import mwaris.dev.currencyxchange.data.remote.responses.LatestCurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LatestCurrenciesApi {
    @GET("/latest.json")
    suspend fun getUpdatedCurrenciesData(
        @Query("app_id") appId : String = BuildConfig.APP_ID
    ): LatestCurrenciesResponse
}
