package com.example.assignment.retrofil

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class MyNetwork(private val context: Context) {
    interface Network{
        fun isNetwork(boolean: Boolean)
    }
        fun checkNetWork(check: Network):Unit{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return check.isNetwork(false)
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return check.isNetwork(false)
            val boolean= networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            check.isNetwork(boolean)
        }else{
            val networkInfo = connectivityManager.activeNetworkInfo ?: return check.isNetwork(false)
             val boolean= networkInfo.isConnected
            check.isNetwork(boolean)
        }
    }
}