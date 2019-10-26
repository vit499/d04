package ru.vit499.d04.http

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import android.net.NetworkInfo
import android.net.NetworkCapabilities
import android.net.Network
import androidx.core.content.ContextCompat.getSystemService







class Link {

    companion object{

        fun isLink(context: Context): Boolean {
            //val a = ConnectivityManager.NetworkCallback()

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo?.isConnected == false
                || networkInfo?.type != ConnectivityManager.TYPE_WIFI
                && networkInfo?.type != ConnectivityManager.TYPE_MOBILE) {
                return false
            }



            return true
        }

    }
}