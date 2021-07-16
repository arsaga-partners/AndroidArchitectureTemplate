import com.android.build.gradle.internal.dsl.BuildType

object ProjectProperty {

    const val APPLICATION_ID = "com.example.app"

    const val MIN_SDK_VERSION = 23

    internal enum class FlavorType {
        prod, stg, dev
    }

    //FIXME:できればかっこいい名前にしたい
    internal enum class BuildTypeType(val defaultExist: Boolean, val action: (BuildType) -> Unit) {
        debug(true, {}),
        release(true, {})
    }

    internal enum class BuildConfig(
        val type: BuildConfigType,
        val value: (FlavorType, BuildTypeType) -> String
    ) {
        IS_DEBUG_LOGGING(BuildConfigType.Boolean, { flavorType, _ ->
            (flavorType != FlavorType.prod).toString()
        }),
        BASE_URL(BuildConfigType.String, { flavorType, _ ->
            if (flavorType == FlavorType.prod) baseUrl("")
            else baseUrl(flavorType.name)
        })
    }

    internal enum class BuildConfigType {
        Boolean, String
    }

    private fun baseUrl(prefix: String?): String = "\"https://${prefix ?: ""}.arsaga.jp/v1/api/\""

}