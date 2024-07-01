package app.dayacore.kds.presentation.config

sealed class ConfigIntent {
    data class DoLoadInit(val isEditConfig: Boolean) : ConfigIntent()
    data class DoUrlToLoadChanged(val urlToLoad: String) : ConfigIntent()
    data class DoOpenWebView(val urlToLoad: String) : ConfigIntent()
}