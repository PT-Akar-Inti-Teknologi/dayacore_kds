package app.dayacore.kds.presentation.webview

import app.dayacore.core.presentation.UiState
import app.dayacore.core.utils.Empty

data class WebViewState(
    val urlToLoad: String = String.Empty,
) : UiState