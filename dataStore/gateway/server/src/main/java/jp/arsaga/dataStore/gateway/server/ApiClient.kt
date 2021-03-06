package jp.arsaga.dataStore.gateway.server

import jp.co.arsaga.extensions.gateway.AbstractApiClient
import retrofit2.Converter
import retrofitApiProvider

object ApiClient : AbstractApiClient<IApiType>() {
    override val baseUrl: String = BuildConfig.BASE_URL

    override fun isDebug(): Boolean = BuildConfig.IS_DEBUG_LOGGING

    override val maxRetryCount: Int = 0

    override val jsonConverterFactory: Converter.Factory = JsonConverter.factory

    override val retrofitApi: IApiType = retrofitApiBuilder
        .run(::retrofitApiProvider)
}