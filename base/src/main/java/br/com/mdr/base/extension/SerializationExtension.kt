package br.com.mdr.base.extension

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type

object SerializationExtension {
    val gson: Gson = GsonBuilder().create()

    inline fun <reified T> String.jsonToObject(): T? {
        return try {
            gson.fromJson(this, T::class.java)
        } catch (e: JsonSyntaxException) {
            e.message?.let { Timber.e(it) }
            null
        }
    }
}