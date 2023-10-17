package mwaris.dev.currencyxchange.data.sync.worker

import androidx.work.CoroutineWorker
import androidx.work.Data
import kotlin.reflect.KClass

private const val WORKER_CLASS_NAME = "WorkerDelegateClassName"

internal fun KClass<out CoroutineWorker>.defaultInputData() =
    Data.Builder()
        .putString(WORKER_CLASS_NAME, qualifiedName)
        .build()