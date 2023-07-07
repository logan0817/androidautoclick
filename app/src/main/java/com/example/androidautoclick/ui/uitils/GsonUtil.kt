package com.example.androidautoclick.ui.uitils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GsonUtil {
    companion object {
        private var gson: Gson = Gson()

        /**
         * @param object Any
         * @return String?
         */
        fun toJsonString(`object`: Any?): String? {
            var gsonString: String? = null
            try {
                gsonString = gson.toJson(`object`)
            } catch (exception: Exception) {
                print("GsonUtil toJsonString() ${exception}")
            }
            return gsonString
        }

        /**
         * @param jsonString serialization result
         * @param <T> cls type
         * @return T?
         */
        fun <T> jsonToBean(jsonString: String?, cls: Class<T>?): T? {
            var t: T? = null
            try {
                t = gson.fromJson(jsonString, cls)
            } catch (exception: Exception) {
                print("GsonUtil jsonToBean() ${exception}")
            }
            return t
        }

        /**
         * @param jsonString serialization result
         * @param <T> cls type
         * @return ArrayList<T>
         */
        fun <T> jsonToList(jsonString: String?, cls: Class<T>?): ArrayList<T> {
            var list: ArrayList<T> = arrayListOf()
            try {
                val type = TypeToken.getParameterized(List::class.java, cls).type
                list = gson.fromJson(jsonString, type)
            } catch (exception: Exception) {
                print("GsonUtil jsonToList() ${exception}")
            }
            return list
        }


        /**
         * @param jsonString map serialization result
         * @param <K> k type
         * @param <V> v type
         * @return Map<K, V>
         */
        fun <K, V> jsonToMap(jsonString: String?, kClazz: Class<K>?, vClazz: Class<V>?): Map<K, V> {
            var map: Map<K, V> = mutableMapOf()
            try {
                map = gson.fromJson(
                    jsonString,
                    TypeToken.getParameterized(MutableMap::class.java, kClazz, vClazz).type
                )
            } catch (exception: Exception) {
                print("GsonUtil jsonToMap() ${exception}")
            }
            return map
        }

        /**
         * @param object Any
         * @param <K> k type
         * @param <V> v type
         * @return Map<K, V>
         */
        fun <K, V> beanToMap(`object`: Any?, kClazz: Class<K>?, vClazz: Class<V>?): Map<K, V> {
            var map: Map<K, V> = mutableMapOf()
            try {
                val jsonString = toJsonString(`object`)
                map = gson.fromJson(
                    jsonString,
                    TypeToken.getParameterized(MutableMap::class.java, kClazz, vClazz).type
                )
            } catch (exception: Exception) {
                print("GsonUtil jsonToMap() ${exception}")
            }
            return map
        }

    }
}