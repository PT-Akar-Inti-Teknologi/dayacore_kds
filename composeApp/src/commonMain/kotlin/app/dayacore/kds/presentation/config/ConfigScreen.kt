package app.dayacore.kds.presentation.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.dayacore.core.composables.CustomViewWrapper
import app.dayacore.core.utils.StateScreenWrapper
import app.dayacore.kds.presentation.webview.WebViewScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch

class ConfigScreen(private val isEditConfig: Boolean = false) : Screen {

    override val key: ScreenKey = "ConfigScreen"

    @Composable
    override fun Content() {
        StateScreenWrapper(
            viewModel = rememberScreenModel { ConfigViewModel() },
            navigator = LocalNavigator.currentOrThrow,
            events = { flow, scope, navigator ->
                scope.launch {
                    flow.collect { event ->
                        when (event) {
                            is ConfigEvent.OnDirectWebView ->
                                navigator.replaceAll(item = WebViewScreen())
                        }
                    }
                }
            },
            content = { viewModel, state ->
                // handle onclick as remember to decrease redundant recompositions
                val actionIntent: (ConfigIntent) -> Unit = remember {
                    return@remember {
                        viewModel.onIntent(it)
                    }
                }

                LifecycleEffect(
                    onStarted = {
                        actionIntent.invoke(ConfigIntent.DoLoadInit(isEditConfig = isEditConfig))
                    }
                )

                ConfigView(
                    state = state,
                    intent = actionIntent
                )
            }
        )
    }

    @Composable
    private fun ConfigView(
        state: ConfigState,
        intent: (ConfigIntent) -> Unit,
    ) {
        val scrollState = rememberScrollState()

        CustomViewWrapper(
            contentBody = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    /* Url Web app */
                    Text(
                        text = "Url Web app",
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                    /* url to load */
                    OutlinedTextField(
                        value = state.urlToLoad,
                        onValueChange = { value ->
                            intent.invoke(ConfigIntent.DoUrlToLoadChanged(urlToLoad = value))
                        },
                        label = { Text(text = "URL to load in Web View") },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    /* button save */
                    Button(
                        onClick = {
                            intent.invoke(
                                ConfigIntent.DoOpenWebView(
                                    urlToLoad = state.urlToLoad
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        enabled = state.urlToLoad.isNotEmpty()
                    ) {
                        Text(text = "Save and Open WebView")
                    }
                }
            }
        )
    }
}