package com.example.wanandroidmvp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.wanandroidmvp.app.App
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.reflect.KProperty

class Preference<T>(val name: String, private val default: T) {

    companion object {
        private val file_name = "wan_android_file"

        private val prefs: SharedPreferences by lazy {
            App.context.getSharedPreferences(file_name, Context.MODE_PRIVATE)
        }


        fun clearPreference() {
            prefs.edit().clear().apply()
        }

        fun clearPreference(key: String) {
            prefs.edit().remove(key).apply()
        }

        fun contains(key: String): Boolean {
            return prefs.contains(key)
        }

        fun getAll(): Map<String, *> {
            return prefs.all
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences(name, default)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        return putSharedPreferences(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSharedPreferences(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default) ?: ""
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> deSerialize(getString(name, serialize(default)) ?: "")
        }
        return res as T
    }


    //序列化
    @Throws(IOException::class)
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var setStr = byteArrayOutputStream.toString("ISO-8859-1")
        setStr = java.net.URLEncoder.encode(setStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return setStr
    }

    //反序列化
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    private fun <A> deSerialize(str: String): A {
        val redStr = java.net.URLEncoder.encode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}