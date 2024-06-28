package app.dayacore.kds.presentation.webview

sealed class WebViewIntent {
    data object DoBackPress : WebViewIntent()
    data object DoLoadInit : WebViewIntent()
}