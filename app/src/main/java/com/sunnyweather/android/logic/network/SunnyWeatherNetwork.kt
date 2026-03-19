package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.RealtimeResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun getDailyWeather(lng:String,lat:String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun getRealtimeWeather(lng: String,lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

//    suspend fun getRealtimeWeather(lng: String, lat: String): RealtimeResponse =
//        weatherService.getRealtimeWeather(lng, lat).await()


    private val placeService = ServiceCreator.create(PlaceService::class.java)

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

//    private suspend fun <T> Call<T>.await(): T {
//        return suspendCancellableCoroutine { continuation ->
//            enqueue(object : Callback<T> {
//                override fun onResponse(call: Call<T>, response: Response<T>) {
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        if (body != null) {
//                            continuation.resume(body)
//                        } else {
//                            // 成功但 body 为 null（极少见，可能是解析问题）
//                            continuation.resumeWithException(RuntimeException("Response body is null but request succeeded"))
//                        }
//                    } else {
//                        // HTTP 错误，提取错误信息
//                        val errorMsg = response.errorBody()?.string() ?: "Unknown error"
//                        continuation.resumeWithException(RuntimeException("HTTP ${response.code()}: $errorMsg"))
//                    }
//                }
//
//                override fun onFailure(call: Call<T>, t: Throwable) {
//                    // 网络失败（如超时、无网络）
//                    continuation.resumeWithException(t)
//                }
//            })
//
//            // 处理协程取消：取消网络请求
//            continuation.invokeOnCancellation {
//                try {
//                    this.cancel()
//                } catch (e: Exception) {
//                    // 忽略取消时的异常
//                }
//            }
//        }
//    }
}