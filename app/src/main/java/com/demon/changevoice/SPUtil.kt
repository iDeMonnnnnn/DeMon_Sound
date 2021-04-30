package com.demon.changevoice

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


/**
 * @author DeMon
 * Created on 2021/4/29.
 * E-mail 757454343@qq.com
 * Desc:
 */
object SPUtil {

    private var mPref: SharedPreferences? = null

    init {
        mPref = App.mContext.getSharedPreferences(this.javaClass.simpleName, MODE_PRIVATE)
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param data
     */
    fun save(key: String?, data: Any) {
        val editor = mPref?.edit()
        when (data) {
            is String -> {
                editor?.putString(key, data)
            }
            is Int -> {
                editor?.putInt(key, data)
            }
            is Boolean -> {
                editor?.putBoolean(key, data)
            }
            is Float -> {
                editor?.putFloat(key, data)
            }
            is Long -> {
                editor?.putLong(key, data)
            }
            else -> {
                editor?.putString(key, data.toString())
            }
        }
        editor?.apply()
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    fun get(key: String?, defaultObject: Any): Any {
        return when (defaultObject) {
            is String -> {
                mPref?.getString(key, defaultObject) ?: ""
            }
            is Int -> {
                mPref?.getInt(key, defaultObject) ?: 0
            }
            is Boolean -> {
                mPref?.getBoolean(key, defaultObject) ?: false
            }
            is Float -> {
                mPref?.getFloat(key, defaultObject) ?: 0f
            }
            is Long -> {
                mPref?.getLong(key, defaultObject) ?: 0L
            }
            else -> ""
        }
    }
}