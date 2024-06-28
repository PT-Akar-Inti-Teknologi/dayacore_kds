package app.dayacore.kds.presentation.webview

sealed class WebViewEvent {
    data object OnDirectBack : WebViewEvent()
}