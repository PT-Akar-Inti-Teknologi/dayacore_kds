package app.dayacore.kds.presentation.config

import app.dayacore.core.presentation.BaseViewModel
import app.dayacore.domain.usecase.kds.KdsSettingsGetUrlToLoadUseCase
import app.dayacore.domain.usecase.kds.KdsSettingsSaveUrlToLoadUseCase
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class ConfigViewModel : BaseViewModel<ConfigState, ConfigEvent>(ConfigState()) {

    /* preference */
    private val kdsSettingsGetUrlToLoadUseCase: KdsSettingsGetUrlToLoadUseCase by inject()
    private val kdsSettingsSaveUrlToLoadUseCase: KdsSettingsSaveUrlToLoadUseCase by inject()

    fun onIntent(intent: ConfigIntent) {
        when (intent) {
            is ConfigIntent.DoLoadInit ->
                doLoadInit(isEditConfig = intent.isEditConfig)

            is ConfigIntent.DoUrlToLoadChanged ->
                // update state
                setState { it.copy(urlToLoad = intent.urlToLoad) }

            is ConfigIntent.DoOpenWebView ->
                doOpenWebView(urlToLoad = intent.urlToLoad)
        }
    }

    private fun doLoadInit(isEditConfig: Boolean) = screenModelScope.launch {
        // get kds setting from preference
        val urlToLoad = kdsSettingsGetUrlToLoadUseCase.execute()
        // validate ui
        println("doLoadInit-urlToLoad : $urlToLoad")
        if (isEditConfig.not() && urlToLoad.isNotEmpty()) {
            // send event
            sendEvent(event = ConfigEvent.OnDirectWebView)
            return@launch
        }
        // update state
        setState { it.copy(urlToLoad = urlToLoad) }
    }

    private fun doOpenWebView(urlToLoad: String) = screenModelScope.launch {
        // save kds setting to preference
        kdsSettingsSaveUrlToLoadUseCase.execute(urlToLoad = urlToLoad).also { success ->
            if (success) {
                // send event
                sendEvent(event = ConfigEvent.OnDirectWebView)
            }
        }
    }
}