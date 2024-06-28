package app.dayacore.kds.core.jsbridge

import app.dayacore.core.utils.toJson
import app.dayacore.domain.usecase.kds.KdsSettingsGetUseCase
import app.dayacore.kds.data.KdsSettingsDto
import app.dayacore.kds.data.toDto
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class KdsSettingsGetJsMessageHandler : IJsMessageHandler {

    /* use case */
    private val kdsSettingsGetUseCase: KdsSettingsGetUseCase by KoinJavaComponent.inject(
        clazz = KdsSettingsGetUseCase::class.java
    )

    override fun methodName(): String {
        return "GetKdsSettings"
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
            // set callback to js
            callback(
                kdsSettingsData
                    .toDto()
                    .toJson(serializer = KdsSettingsDto.serializer())
            )
        }
    }
}
