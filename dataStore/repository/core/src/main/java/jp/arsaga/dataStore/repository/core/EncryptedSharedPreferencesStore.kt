package jp.arsaga.dataStore.repository.core

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import jp.arsaga.dataStore.gateway.local.SharedPreferenceController
import jp.arsaga.domain.entity.core.type.LocalDataKey
import jp.arsaga.domain.entity.core.type.LocalSaveDataFileName
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object EncryptedSharedPreferencesStore {
    private val store = ConcurrentHashMap<LocalSaveDataFileName, SharedPreferences>()

    private val lockCache = mutableMapOf<String, Any>()

    suspend fun LocalSaveDataFileName.getSharedPreferences(
        context: Context,
        param: String
    ): SharedPreferences? = suspendCoroutine { continuation ->
        val fileName = fileName(param)
        val lock = lockCache.getOrPut(fileName) { Any() }
        runCatching {
            synchronized(lock) {
                generate(context, this, fileName)
            }.also {
                lockCache.remove(fileName)
            }
        }.onSuccess { continuation.resume(it) }
            .onFailure {
                Timber.e("EncryptedSharedPreferences crashed:$it")
                continuation.resume(null)
            }.getOrNull()
    }

    private fun generate(
        context: Context,
        key: LocalSaveDataFileName,
        fileName: String
    ): SharedPreferences = store.getOrPut(key) {
        EncryptedSharedPreferences.create(
            fileName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    suspend fun <T>LocalDataKey<T>.put(
        context: Context,
        value: T,
        param: String = ""
    ) {
        SharedPreferenceController.put(
            saveFile().getSharedPreferences(context, param)?.edit(),
            this,
            value
        )
    }

    suspend fun LocalDataKey.Int.get(
        context: Context,
        param: String = ""
    ): Int? = saveFile().getSharedPreferences(context, param)?.let {
        SharedPreferenceController.get(it, this)
    }

    suspend fun LocalDataKey.Long.get(
        context: Context,
        param: String = ""
    ): Long? = saveFile().getSharedPreferences(context, param)?.let {
        SharedPreferenceController.get(it, this)
    }

    suspend fun LocalDataKey.Float.get(
        context: Context,
        param: String = ""
    ): Float? = saveFile().getSharedPreferences(context, param)?.let {
        SharedPreferenceController.get(it, this)
    }

    suspend fun LocalDataKey.Boolean.get(
        context: Context,
        param: String = ""
    ): Boolean? = saveFile().getSharedPreferences(context, param)?.let {
        SharedPreferenceController.get(it, this)
    }

    suspend fun LocalDataKey.String.get(
        context: Context,
        param: String = ""
    ): String? = saveFile().getSharedPreferences(context, param)?.let {
        SharedPreferenceController.get(it, this)
    }

}