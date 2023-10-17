package mwaris.dev.currencyxchange.di

import android.annotation.SuppressLint
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mwaris.dev.currencyxchange.BuildConfig
import mwaris.dev.currencyxchange.utils.ConnectivityManagerNetworkMonitor
import mwaris.dev.currencyxchange.utils.NetworkMonitor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrenciesAPIClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_DEFAULT = 30L

    @Provides
    @Singleton
    fun providesNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return ConnectivityManagerNetworkMonitor(context)
    }

    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun providesOkhttpClient(): OkHttpClient.Builder {
        val connectionPool = ConnectionPool()
        return OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .connectTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
    }

    @Provides
    @Named("BASE_URL")
    fun provideAppBaseUrl(): String = BuildConfig.BASE_API_URL

    @CurrenciesAPIClient
    @Provides
    @Singleton
    fun providesReposRetrofitInstance(
        @ApplicationContext context: Context,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(provideAppBaseUrl())
            .addConverterFactory(providesConverterFactory())
            .client(
                providesOkhttpClient()
                    .appendLogs(context)
                    .build()
            )
            .build()
    }

}

@SuppressLint("StaticFieldLeak")
private lateinit var chuckInterceptor: ChuckerInterceptor

private fun OkHttpClient.Builder.appendLogs(context: Context): OkHttpClient.Builder {
    if (!::chuckInterceptor.isInitialized) {
        chuckInterceptor = ChuckerInterceptor.Builder(context)
            .collector(
                ChuckerCollector(
                    context,
                    retentionPeriod = RetentionManager.Period.ONE_HOUR
                )
            )
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }
    if (BuildConfig.DEBUG) {
        addInterceptor(
            HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        addInterceptor(chuckInterceptor)
    }
    return this

}