package com.demon.changevoice

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author DeMon
 * Created on 2021/4/29.
 * E-mail 757454343@qq.com
 * Desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this.applicationContext
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }
}