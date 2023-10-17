package mwaris.dev.currencyxchange.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mwaris.dev.currencyxchange.data.local.db.XchangeDatabase
import mwaris.dev.currencyxchange.data.local.dao.CurrenciesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
    ): XchangeDatabase = Room.databaseBuilder(
        context,
        XchangeDatabase::class.java,
        "xchange-database",
    ).build()

    @Provides
    fun providesAppDao(
        appDatabase: XchangeDatabase
    ): CurrenciesDao {
        return appDatabase.currenciesDao()
    }
}