package mwaris.dev.currencyxchange.data.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import mwaris.dev.currencyxchange.data.sync.worker.SyncWorker

internal const val SyncWorkNamePeriodic = "SyncWorkNamePeriodic"
object Sync {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(
                SyncWorkNamePeriodic,
                ExistingPeriodicWorkPolicy.KEEP,
                SyncWorker.startUpPeriodicWork(),
            )
        }
    }
}