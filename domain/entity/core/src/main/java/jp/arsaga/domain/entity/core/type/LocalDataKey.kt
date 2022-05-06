package jp.arsaga.domain.entity.core.type


interface LocalSaveDataFileNameInterface {
    fun fileName(param: String): String
}

enum class LocalSaveDataFileName : LocalSaveDataFileNameInterface {
    USER_DEFAULT {
        override fun fileName(param: String): String = name
    },
    ALL_USER {
        override fun fileName(param: String): String = name
    }
}

sealed interface LocalDataKey<T> {
    val defaultValue: T
    fun key(): kotlin.String
    fun saveFile(): LocalSaveDataFileName = LocalSaveDataFileName.USER_DEFAULT
    enum class Int : LocalDataKey<kotlin.Int>
    enum class Long : LocalDataKey<kotlin.Long>
    enum class Float : LocalDataKey<kotlin.Float>
    enum class Boolean : LocalDataKey<kotlin.Boolean> {
        IsInitBoot {
            override val defaultValue: kotlin.Boolean = true
            override fun key(): kotlin.String = name
            override fun saveFile() = LocalSaveDataFileName.ALL_USER
        }
    }

    enum class String : LocalDataKey<kotlin.String?> {
        Password {
            override val defaultValue: kotlin.String = ""
            override fun key(): kotlin.String = name
        },
    }
}