import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.dayacore.di.db.DBDataSource
import app.dayacore.di.db.usecase.DBKdsUseCaseModule
import app.dayacore.di.startKoinWithShared
import app.dayacore.kds.App
import app.dayacore.kds.di.EventBusModule
import app.dayacore.kds.presentation.config.ConfigScreen
import cafe.adriel.voyager.core.screen.Screen
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

fun main() = application {
    // init start koin
    startKoinWithShared(
        moduleList = listOf(
            // event bus
            EventBusModule,
            // data
            DBDataSource,
            // use case
            DBKdsUseCaseModule
        )
    )

    initWindow()
}

@Composable
private fun ApplicationScope.initWindow() {
    var newScreen by remember { mutableStateOf<Screen?>(null) }

    Window(
        title = "KDS DayaCore",
        state = rememberWindowState(placement = WindowPlacement.Floating),
        resizable = true,
        onCloseRequest = ::exitApplication
    ) {
        MenuBar {
            Menu(text = "Menu", mnemonic = 'M') {
                Item(
                    text = "Open Config",
                    onClick = {
                        newScreen = ConfigScreen(isEditConfig = true)
                    },
                    shortcut = KeyShortcut(
                        Key.C, ctrl = true
                    )
                )
            }
        }
        jvmMainBuilder {
            App(newScreen = newScreen)
        }
    }
}

@Composable
internal fun jvmMainBuilder(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    kcefPathName: String = "kcef-bundle",
    initSuccess: @Composable () -> Unit,
) {
    var restartRequired by remember { mutableStateOf(false) }
    var downloading by remember { mutableStateOf(0F) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        withContext(context = dispatcher) {
            KCEF.init(builder = {
                installDir(File(kcefPathName))
                progress {
                    onDownloading {
                        downloading = it
                    }
                    onInitialized {
                        initialized = true
                    }
                }
                release("jbr-release-17.0.10b1087.23")
                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                it?.printStackTrace()
            }, onRestartRequired = {
                restartRequired = true
            })
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (restartRequired) {
            Text(
                text = "Restart required.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            if (initialized) {
                initSuccess.invoke()
            } else {
                Text(
                    text = "Downloading $downloading%",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
        }
    }
}