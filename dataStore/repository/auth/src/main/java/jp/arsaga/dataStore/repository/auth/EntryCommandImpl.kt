package jp.arsaga.dataStore.repository.auth

import android.content.Context
import jp.arsaga.dataStore.gateway.local.ReactiveLocalDataSaver
import jp.arsaga.domain.entity.core.type.LocalDataKey
import jp.arsaga.domain.service.auth.EntryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class EntryCommandImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : EntryService.Command {

    private val reactiveLocalDataSaver = mutableSetOf<ReactiveLocalDataSaver<*>>()

    override fun saveLocalCacheData(flow: Flow<String?>, localDataKey: LocalDataKey<String?>) {
        ReactiveLocalDataSaver(
            coroutineScope, flow, localDataKey
        ) { context.getSharedPreferences("test", Context.MODE_PRIVATE) }
            .apply(reactiveLocalDataSaver::add)
    }

    override fun login() {
    }

    override fun register() {
    }

}