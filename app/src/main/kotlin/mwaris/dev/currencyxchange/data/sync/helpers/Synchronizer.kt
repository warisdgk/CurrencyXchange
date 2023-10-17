package mwaris.dev.currencyxchange.data.sync.helpers

interface Synchronizer {
    suspend fun Syncable.sync() = this@sync.syncWith()
}

interface Syncable {
    suspend fun syncWith(): Boolean
}