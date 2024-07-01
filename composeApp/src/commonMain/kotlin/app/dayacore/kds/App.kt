package app.dayacore.kds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.dayacore.kds.presentation.config.ConfigScreen
import app.dayacore.kds.theme.getTypography
import app.dayacore.theme.AppTheme
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal fun App(newScreen: Screen?) = AppTheme(
    typography = getTypography()
) {
    // init navigator screen
    Navigator(
        screen = ConfigScreen()
    ) { navigator ->

        CurrentScreen()

        remember(newScreen) {
            if (newScreen == null) return@remember
            // navigate to new screen
            navigator.replaceAll(item = newScreen)
        }
    }
}