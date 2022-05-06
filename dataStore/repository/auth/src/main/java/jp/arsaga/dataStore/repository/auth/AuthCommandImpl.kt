package jp.arsaga.dataStore.repository.auth

import android.content.Context
import jp.arsaga.dataStore.gateway.local.ReactiveLocalDataSaver
import jp.arsaga.dataStore.repository.core.EncryptedSharedPreferencesStore.getSharedPreferences
import jp.arsaga.dataStore.repository.core.TransitionCallbackHandler
import jp.arsaga.domain.entity.core.type.LocalDataKey
import jp.arsaga.domain.entity.core.type.LocalSaveDataFileName
import jp.arsaga.domain.useCase.auth.AuthUseCase
import jp.arsaga.domain.useCase.core.ActivityCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class AuthCommandImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : AuthUseCase.Command<ActivityCallback> {

    private val reactiveLocalDataSaver = mutableSetOf<ReactiveLocalDataSaver<*>>()

    override fun saveLocalCacheData(flow: Flow<String?>, localDataKey: LocalDataKey<String?>) {
        ReactiveLocalDataSaver(
            coroutineScope, flow, localDataKey
        ) { LocalSaveDataFileName.USER_DEFAULT.getSharedPreferences(context, "") }
            .apply(reactiveLocalDataSaver::add)
    }

    override fun login(onSuccess: () -> ActivityCallback) {
        Thread.sleep(1500L)
        TransitionCallbackHandler.post(onSuccess())
    }

    override fun register(onSuccess: () -> ActivityCallback) {
    }

}