package mwaris.dev.currencyxchange.ui;

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mwaris.dev.currencyxchange.utils.NetworkMonitor


@Composable
fun rememberAppNetworkState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    return remember(
        coroutineScope,
        networkMonitor
    ) {
        AppState(
            coroutineScope,
            networkMonitor
        )
    }
}

@Stable
class AppState(
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
}