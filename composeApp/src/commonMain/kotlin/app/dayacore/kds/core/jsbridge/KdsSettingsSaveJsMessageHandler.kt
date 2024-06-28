package app.dayacore.kds.core.jsbridge

import app.dayacore.core.utils.Empty
import app.dayacore.core.utils.fromJson
import app.dayacore.data.local.model.kds.KdsSettingsData
import app.dayacore.domain.usecase.kds.KdsSettingsGetUseCase
import app.dayacore.domain.usecase.kds.KdsSettingsSaveUseCase
import app.dayacore.kds.data.KdsSettingsDto
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class KdsSettingsSaveJsMessageHandler : IJsMessageHandler {

    /* use case */
    private val kdsSettingsGetUseCase: KdsSettingsGetUseCase by KoinJavaComponent.inject(
        clazz = KdsSettingsGetUseCase::class.java
    )
    private val kdsSettingsSaveUseCase: KdsSettingsSaveUseCase by KoinJavaComponent.inject(
        clazz = KdsSettingsSaveUseCase::class.java
    )

    override fun methodName(): String {
        return "SaveKdsSettings"
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        GlobalScope.launch {
            // get kds setting from preference
            val kdsSettingsData = kdsSettingsGetUseCase.execute()
            // get param from js inject
            message.params.fromJson(deserializer = KdsSettingsDto.serializer()).also {
                // save kds setting to preference
                kdsSettingsSaveUseCase.execute(
                    kdsSettings = KdsSettingsData(
                        urlToLoad = kdsSettingsData.urlToLoad,
                        viewMode = it.viewMode ?: String.Empty,
                        visitPurposeId = it.visitPurposeId ?: emptyList(),
                        timeFirstWarning = it.timeFirstWarning ?: 5,
                        timeSecondWarning = it.timeSecondWarning ?: 10,
                        stationsId = it.stationsId ?: emptyList(),
                        printingStationId = it.printingStationId ?: emptyList()
                    )
                )
            }
        }
    }
}
