package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
data class RealtimeResponse(val status:String,val result:Result){
    //这些数据模型类定义在RealtimeResponse里，可以防止与其他接口模型类又同名冲突的情况
    data class Result(val realtime: Realtime)
    data class Realtime(val skycon:String,val temperature:Float,
                        @SerializedName("air_quality")val airQuality :AirQuality)
    data class AirQuality(val aqi:AQI)
    data class AQI(val chn:Float)
}
