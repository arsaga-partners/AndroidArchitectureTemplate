package jp.arsaga.domain.entity.core.type

interface LocalDataKey<T> {
    val defaultValue: T

    enum class Int : LocalDataKey<kotlin.Int>
    enum class Long : LocalDataKey<kotlin.Long>
    enum class Float : LocalDataKey<kotlin.Float>
    enum class Boolean : LocalDataKey<kotlin.Boolean>
    enum class String : LocalDataKey<kotlin.String?>
    enum class StringSet : LocalDataKey<Set<kotlin.String>?>
}