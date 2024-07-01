package app.dayacore.kds.presentation.webview

import app.dayacore.core.presentation.BaseViewModel
import app.dayacore.domain.usecase.kds.KdsSettingsGetUrlToLoadUseCase
import app.dayacore.kds.core.eventbus.StringEventBus
import cafe.adriel.voyager.core.model.screenModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class WebViewViewModel : BaseViewModel<WebViewState, WebViewEvent>(WebViewState()) {
    /* event bus */
    private val eventBus: ChannelEventBus by inject()

    /* preference */
    private val kdsSettingsGetUrlToLoadUseCase: KdsSettingsGetUrlToLoadUseCase by inject()

    init {
        screenModelScope.launch {
            eventBus
                .receiveAsFlow(StringEventBus)
                .collect { eventBus ->
                    // update state
                    setState { it.copy(kdsSettingLog = eventBus.value) }
                }
        }
    }

    fun onIntent(intent: WebViewIntent) {
        when (intent) {
            is WebViewIntent.DoBackPress ->
                sendEvent(event = WebViewEvent.OnDirectBack)

            is WebViewIntent.DoLoadInit ->
                doLoadInit()
        }
    }

    private fun doLoadInit() = screenModelScope.launch {
        // get kds setting from preference
        val urlToLoad = kdsSettingsGetUrlToLoadUseCase.execute()
        // update state
        setState { it.copy(urlToLoad = urlToLoad) }
    }
}