package app.dayacore.kds

import androidx.compose.runtime.Composable
import app.dayacore.kds.presentation.config.ConfigScreen
import app.dayacore.kds.theme.getTypography
import app.dayacore.theme.AppTheme
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal fun App() = AppTheme(
    typography = getTypography()
) {
    // init navigator screen
    Navigator(screen = ConfigScreen())
}