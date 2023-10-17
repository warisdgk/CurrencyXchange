package mwaris.dev.currencyxchange.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import mwaris.dev.currencyxchange.R
import mwaris.dev.currencyxchange.ui.convert.CurrencyConversionScreen
import mwaris.dev.currencyxchange.ui.theme.CurrencyXchangeTheme
import mwaris.dev.currencyxchange.utils.NetworkMonitor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyXchangeTheme {
                CurrencyXchangeApp(
                    networkMonitor
                )
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun CurrencyXchangeApp(
        networkMonitor: NetworkMonitor,
        appState: AppState = rememberAppNetworkState(
            networkMonitor = networkMonitor,
        ),
    ) {

        val isOffline by appState.isOffline.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }

        val notConnectedMessage = stringResource(R.string.not_connected)
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = SnackbarDuration.Indefinite,
                )
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            modifier = Modifier.testTag("top-app-bar")
                        )
                    })
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CurrencyConversionScreen()
            }
        }
    }
}