package ru.vit499.d04.http


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import ru.vit499.d04.util.Logm


class Link {

    companion object{

        fun isLink1(context: Context): Boolean {
            //val a = ConnectivityManager.NetworkCallback()

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

//            val networkInfo = connectivityManager.activeNetworkInfo
//
//            if (networkInfo?.isConnected == false
//                || networkInfo?.type != ConnectivityManager.TYPE_WIFI
//                && networkInfo?.type != ConnectivityManager.TYPE_MOBILE) {
//                return false
//            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val a = cm.allNetworks
                Logm.aa("is link ??? -------------------- ")
                for(i in 0 until a.size) {
                    Logm.aa("link ")
                    a[i]?.let { Logm.aa(a[i].toString()) }
                }
                //Logm.aa(a.toString())


            }

            return true
        }


        //@TargetApi(21)
        fun isLink(context: Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)//TRANSPORT_WIFI
            val build = builder.build()
            connectivityManager.requestNetwork(build, networkCallback)
            return true
        }


        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Logm.aa("onAvailable   ----------------")

            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Logm.aa("onLost   ----------------")
            }
            override fun onUnavailable() {
                super.onUnavailable()
                Logm.aa("onUnavailable   ----------------")
            }
        }

    }
}