package jp.arsaga.dataStore.repository.auth

import android.content.Context
import jp.arsaga.dataStore.gateway.local.SharedPreferenceController
import jp.arsaga.dataStore.repository.core.EncryptedSharedPreferencesStore.getSharedPreferences
import jp.arsaga.domain.entity.core.type.LocalDataKey
import jp.arsaga.domain.entity.core.type.LocalSaveDataFileName
import jp.arsaga.domain.useCase.auth.AuthUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthQueryImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : AuthUseCase.Query {

    override fun queryLocalCacheData(
        localDataKey: LocalDataKey<String?>,
        callback: (String?) -> Unit
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            SharedPreferenceController.get(
                LocalSaveDataFileName.USER_DEFAULT.getSharedPreferences(context, ""),
                localDataKey as LocalDataKey.String
            ).run {
                callback(this)
            }
        }
    }
}