package com.mvvm.girishdemo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

class Utils @Inject constructor(private val context: Context) {

    /**
     * To know if we are connected to the internet or not
     */
    fun isConnectedToInternet(): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }

    /**
     * To check for the flag in the shared preferences if the database is created or not
     */
    fun isDataBaseCreated(): Boolean {
        val sharedPreference = context.getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(Constants.IS_DATABASE_CREATED, false)
    }
}