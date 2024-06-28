package app.dayacore.kds.di

import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusLogger
import org.koin.dsl.module

val EventBusModule = module {
    single {
        ChannelEventBus(
            ChannelEventBusLogger.noop()
        )
    }
}
