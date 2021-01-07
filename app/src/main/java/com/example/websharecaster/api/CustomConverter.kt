package com.example.websharecaster.api

import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class CustomConverter : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        annotations.forEach {
            if (it.annotationClass == Xml::class) {
                return TikXmlConverterFactory.create()
                    .responseBodyConverter(type, annotations, retrofit)
            }
            if (it.annotationClass == Json::class) {
                return GsonConverterFactory.create()
                    .responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }
}