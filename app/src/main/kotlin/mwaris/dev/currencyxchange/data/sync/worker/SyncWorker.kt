package mwaris.dev.currencyxchange.data.sync.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mwaris.dev.currencyxchange.data.repositories.CurrenciesRepository
import mwaris.dev.currencyxchange.di.AppDispatchers
import mwaris.dev.currencyxchange.di.Dispatcher
import mwaris.dev.currencyxchange.data.sync.helpers.SyncConstraints
import mwaris.dev.currencyxchange.data.sync.helpers.Synchronizer
import mwaris.dev.currencyxchange.data.sync.helpers.syncForegroundInfo
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val currenciesRepository: CurrenciesRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val syncedSuccessfully = async { currenciesRepository.sync() }.await()
        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        fun startUpPeriodicWork() = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.defaultInputData())
            .build()
    }
}