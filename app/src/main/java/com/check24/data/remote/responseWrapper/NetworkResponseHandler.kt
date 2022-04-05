package com.check24.data.remote.responseWrapper

import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


interface NetworkResponseHandler {
    suspend fun <T> getResponse(
        request: suspend () -> Response<T>, defaultErrorMessage: String
    ): Resource<T>
}

@Singleton
class NetworkResponseHandlerImp @Inject constructor() : NetworkResponseHandler {

    override suspend fun <T> getResponse(
        request: suspend () -> Response<T>, defaultErrorMessage: String
    ): Resource<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                val resultBody = result.body()
                println("${resultBody}")
                if (resultBody != null) Resource.success<T>(result.body()) else Resource.error(
                    defaultErrorMessage + "No Response From Server"
                )
            } else Resource.error<T>(defaultErrorMessage + result.errorBody()?.toString())
                .also { println(result.errorBody()?.toString()) }
        } catch (e: Throwable) {
            /*
            Handle Any Throwable from the Network Call Or Json Parsing
             */
            Resource.error<T>(defaultErrorMessage).also { println(e.stackTraceToString()) }
        }
    }
}