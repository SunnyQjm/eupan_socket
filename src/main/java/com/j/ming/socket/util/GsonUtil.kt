package com.j.ming.socket.util

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by sunny on 17-11-17.
 */
object GsonUtil {
    @JvmStatic private val INSTANCE = Gson()

    @JvmStatic
    fun <T> json2Bean(json: String, beanClass: Class<T>): T = INSTANCE.fromJson<T>(json, beanClass)

    @JvmStatic
    fun <T> json2Bean(json: String, type: Type): T = INSTANCE.fromJson<T>(json, type)

    @JvmStatic
    fun bean2Json(obj: Any): String = INSTANCE.toJson(obj)

}

fun Any.toJson(): String {
    return GsonUtil.bean2Json(this)
}

fun <T> String.toBean(beanClass: Class<T>): T{
    return GsonUtil.json2Bean(this, beanClass)
}

fun <T> String.toBean(type: Type): T{
    return GsonUtil.json2Bean(this, type)
}