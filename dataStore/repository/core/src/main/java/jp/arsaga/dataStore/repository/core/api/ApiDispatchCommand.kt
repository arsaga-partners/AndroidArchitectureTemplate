package jp.arsaga.dataStore.repository.core.api

import android.app.Activity
import jp.arsaga.dataStore.repository.core.TransitionCallbackHandler
import jp.co.arsaga.extensions.gateway.AbstractApiDispatchCommand
import jp.co.arsaga.extensions.gateway.ApiContext
import jp.co.arsaga.extensions.gateway.NormalApiContext
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response
import java.net.UnknownHostException

class ApiDispatchCommand<Res, Req>(
    apiCall: (Req?) -> (suspend () -> Response<Res>)?,
    apiContext: ApiContext<Res, Req>
) : AbstractApiDispatchCommand<Res, Req>(apiCall, apiContext) {

    companion object {
        fun <Res> defaultApiContext(
            callback: suspend (Res) -> Unit,
            coroutineScope: CoroutineScope
        ) = defaultApiContext<Res, Any>(callback, coroutineScope, null)

        fun <Res, Req> defaultApiContext(
            callback: suspend (Res) -> Unit,
            coroutineScope: CoroutineScope,
            request: Req?
        ) = NormalApiContext(callback, coroutineScope, request).let {
            ApiContext(
                callback = it.callback,
                coroutineScope = it.coroutineScope,
                request = it.request,
                fallback = defaultErrorHandling(it),
                serverFallback = defaultServerErrorHandling(it)
            )
        }

        fun <Res, Req> defaultErrorHandling(
            normalApiContext: NormalApiContext<Res, Req>,
            adaptiveHandler: (Activity, Response<Res>, json: String) -> Unit? = { _, _, _ -> null }
        ): suspend (Response<Res>, json: String) -> Unit = { response, json ->
            TransitionCallbackHandler.post { activity ->
                adaptiveHandler(activity, response, json)?.let { return@post }
                when (response.code()) {
                    401 -> {
                    }
                    503 -> {
                        errorMainteHandling(normalApiContext.coroutineScope, activity)
                    }
                    403 -> {
                    }
                    500 -> {
                    }
                    else -> {
                    }
                }
            }
        }

        fun errorHandling(
            errorResponse: String
        ): String? = null.apply {
            TODO()
        }

        fun getErrorStatus(
            errorResponse: String
        ): String? = null.apply {
            TODO()
        }

        private fun errorMainteHandling(
            coroutineScope: CoroutineScope,
            activity: Activity
        ) {

        }

        fun <Res, Req> defaultServerErrorHandling(
            normalApiContext: NormalApiContext<Res, Req>
        ): suspend (Throwable) -> Unit = { throwable ->
            when (throwable) {
                is UnknownHostException -> TransitionCallbackHandler.post { it ->

                }
            }
        }
    }
}