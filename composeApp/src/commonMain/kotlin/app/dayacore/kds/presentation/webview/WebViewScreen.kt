package app.dayacore.kds.presentation.webview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.dayacore.core.composables.label.CustomText
import app.dayacore.core.utils.StateScreenWrapper
import app.dayacore.kds.core.dummy.HtmlRes
import app.dayacore.kds.core.jsbridge.KdsSettingsGetJsMessageHandler
import app.dayacore.kds.core.jsbridge.KdsSettingsSaveJsMessageHandler
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebViewScreen : Screen {

    override val key: ScreenKey = "WebViewScreen"

    @Composable
    override fun Content() {
        StateScreenWrapper(
            viewModel = rememberScreenModel { WebViewViewModel() },
            navigator = LocalNavigator.currentOrThrow,
            events = { flow, scope, navigator ->
                scope.launch {
                    flow.collect { event ->
                        when (event) {
                            is WebViewEvent.OnDirectBack ->
                                navigator.pop()
                        }
                    }
                }
            },
            content = { viewModel, state ->
                // handle onclick as remember to decrease redundant recompositions
                val actionIntent: (WebViewIntent) -> Unit = remember {
                    return@remember {
                        viewModel.onIntent(it)
                    }
                }

                LifecycleEffect(
                    onStarted = {
                        actionIntent.invoke(WebViewIntent.DoLoadInit)
                    }
                )

                WebViewView(
                    state = state,
                )
            }
        )
    }

    @Composable
    private fun WebViewView(
        state: WebViewState,
    ) {
//        val webViewState = rememberWebViewState(url = state.urlToLoad)
        val webViewState = rememberWebViewStateWithHTMLData(data = HtmlRes.html)
        val navigator = rememberWebViewNavigator()
        val jsBridge = rememberWebViewJsBridge(navigator)

        LaunchedEffect(Unit) {
            initWebView(webViewState)
            initJsBridge(jsBridge)
        }

        LaunchedEffect(key1 = state.urlToLoad) {
            delay(800)
            if (webViewState.lastLoadedUrl == null && state.urlToLoad.isNotEmpty()) {
                navigator.loadUrl(state.urlToLoad)
            }
        }

        val loadingState = webViewState.loadingState
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = { loadingState.progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (state.kdsSettingLog.isNotEmpty()) {
                CustomText(
                    text = state.kdsSettingLog,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            WebView(
                state = webViewState,
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                ,
                navigator = navigator,
                webViewJsBridge = jsBridge
            )
        }
    }

    private fun initWebView(webViewState: com.multiplatform.webview.web.WebViewState) {
        webViewState.webSettings.apply {
            zoomLevel = 1.0
            isJavaScriptEnabled = true
            logSeverity = KLogSeverity.Debug
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            customUserAgentString =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/625.20 (KHTML, like Gecko) Version/14.3.43 Safari/625.20"
        }
    }

    private fun initJsBridge(jsBridge: WebViewJsBridge) {
        jsBridge.register(KdsSettingsGetJsMessageHandler())
        jsBridge.register(KdsSettingsSaveJsMessageHandler())
    }
}