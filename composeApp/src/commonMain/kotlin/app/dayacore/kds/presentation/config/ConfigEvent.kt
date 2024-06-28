package app.dayacore.kds.presentation.config

sealed class ConfigEvent {
    data object OnDirectWebView : ConfigEvent()
}