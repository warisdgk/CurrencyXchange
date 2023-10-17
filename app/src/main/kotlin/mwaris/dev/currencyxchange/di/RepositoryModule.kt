package mwaris.dev.currencyxchange.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mwaris.dev.currencyxchange.data.local.dao.CurrenciesDao
import mwaris.dev.currencyxchange.data.remote.datasource.CurrenciesRemoteDataSource
import mwaris.dev.currencyxchange.data.remote.apis.LatestCurrenciesApi
import mwaris.dev.currencyxchange.data.remote.datasource.CurrenciesDataSource
import mwaris.dev.currencyxchange.data.repositories.CurrenciesRepository
import mwaris.dev.currencyxchange.data.repositories.ICurrenciesRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesRemoteDataSource(
        currenciesApi: LatestCurrenciesApi
    ): CurrenciesDataSource {
        return CurrenciesRemoteDataSource(currenciesApi)
    }

    @Provides
    @Singleton
    fun providesCurrencyAPI(
        @CurrenciesAPIClient retrofit: Retrofit
    ): LatestCurrenciesApi {
        return retrofit.create(LatestCurrenciesApi::class.java)
    }

    @Provides
    @Singleton
    fun providesCurrencyConversionRepository(
        currenciesDao: CurrenciesDao,
        currenciesDataSource: CurrenciesDataSource,
    ): ICurrenciesRepository {
        return CurrenciesRepository(
            currenciesDao,
            currenciesDataSource
        )
    }
}