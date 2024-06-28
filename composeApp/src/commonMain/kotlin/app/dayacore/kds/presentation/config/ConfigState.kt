package app.dayacore.kds.presentation.config

import app.dayacore.core.presentation.UiState
import app.dayacore.core.utils.Empty

data class ConfigState(
    val urlToLoad: String = String.Empty,
) : UiState