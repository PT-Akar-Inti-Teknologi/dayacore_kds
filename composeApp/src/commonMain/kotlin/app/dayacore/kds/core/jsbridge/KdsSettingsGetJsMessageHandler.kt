package app.dayacore.kds.core.jsbridge

import app.dayacore.core.utils.Empty
import app.dayacore.core.utils.toJson
import app.dayacore.domain.usecase.kds.KdsSettingsGetUseCase
import app.dayacore.kds.core.eventbus.StringEventBus
import app.dayacore.kds.data.KdsSettingsDto
import app.dayacore.kds.data.toDto
import com.hoc081098.channeleventbus.ChannelEventBus
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent

class KdsSettingsGetJsMessageHandler : IJsMessageHandler, KoinComponent {
    /* event bus */
    private val eventBus: ChannelEventBus by inject()

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
        var stringLog = String.Empty
        GlobalScope.launch {
            stringLog = stringLog.plus("Method Name : ${methodName()}\n=================\n")
            // get param from js inject
            stringLog = stringLog.plus("Param : ${message.params}\n=================\n")
            // get kds setting from preference
            val kdsSettingsData = kdsSettingsGetUseCase.execute()
            // build serializer
            val kdsSettingsSerializer = kdsSettingsData
                .toDto()
                .toJson(serializer = KdsSettingsDto.serializer())
            // set callback to js
            callback(
                kdsSettingsSerializer
            )
            // set response
            stringLog = stringLog.plus("Response : $kdsSettingsSerializer\n")
            // send event bus
            eventBus.send(StringEventBus(value = stringLog))
        }
    }
}
