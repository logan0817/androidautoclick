package com.example.androidautoclick.ui.uitils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
object CommonPreferencesUtil {
    fun init(application: Application) {
        prefs = application.getSharedPreferences(
            SHARED_PREF_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    private var prefs: SharedPreferences? = null

    private const val SHARED_PREF_FILE_NAME = "com.android.androidautoclick.commonpreferences"
    const val SUPPORTEDLOCALES_KEY = "supportedLocales.key"


    @Suppress("UNCHECKED_CAST")
    fun getValue(key: String, default: Any): Any = with(prefs) {
        return when (default) {
            is Int -> this?.getInt(key, default)
            is String -> this?.getString(key, default)
            is Long -> this?.getLong(key, default)
            is Float -> this?.getFloat(key, default)
            is Boolean -> this?.getBoolean(key, default)
            else -> throw IllegalArgumentException("SharedPreferences Type error")
        } ?: default
    }


    fun getString(key: String, default: String = ""): String {
        return getValue(key, default) as String? ?: default
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getValue(key, default) as Int? ?: default
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getValue(key, default) as Long? ?: default
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getValue(key, default) as Boolean? ?: default
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return getValue(key, default) as Float? ?: default
    }

    fun putDataList(listTag: String?, datalist: List<String?>?) {
        if (null == datalist || datalist.size <= 0) return
        //Convert to json data and save
        val Json = GsonUtil.toJsonString(datalist)
        prefs?.edit()?.putString(listTag, Json)?.commit()
    }

    fun getDataList(tag: String?): List<String?>? {
        val datalist: List<String?>
        val Json: String = prefs?.getString(tag, null) ?: return null
        datalist = GsonUtil.jsonToList(Json, String::class.java)
        return datalist
    }

    fun saveValue(key: String, value: Any) = with(prefs?.edit()) {
        when (value) {
            is Long -> this?.putLong(key, value)
            is Int -> this?.putInt(key, value)
            is String -> this?.putString(key, value)
            is Float -> this?.putFloat(key, value)
            is Boolean -> this?.putBoolean(key, value)
            else -> throw IllegalArgumentException("SharedPreferences 类型错误")
        }?.apply()
    }

    fun commitValue(key: String, value: Any) = with(prefs?.edit()) {
        when (value) {
            is Long -> this?.putLong(key, value)
            is Int -> this?.putInt(key, value)
            is String -> this?.putString(key, value)
            is Float -> this?.putFloat(key, value)
            is Boolean -> this?.putBoolean(key, value)
            else -> throw IllegalArgumentException("SharedPreferences 类型错误")
        }?.commit()
    }

    /**
     *
     */
    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }

    /**
     *
     */
    fun remove(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }

}