package app.dayacore.kds.core.eventbus

import com.hoc081098.channeleventbus.ChannelEvent
import com.hoc081098.channeleventbus.ChannelEventKey

data class StringEventBus(val value: String) :
    ChannelEvent<StringEventBus> {
    override val key get() = Key

    companion object Key : ChannelEventKey<StringEventBus>(StringEventBus::class)
}
