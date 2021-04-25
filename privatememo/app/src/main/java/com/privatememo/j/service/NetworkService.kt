package com.privatememo.j.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log


class NetworkService : Service() {

    var networkConnectionCheck: NetworkCallBack? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("NetworkService","서비스 onCreate()")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //LOLLIPOP Version 이상..
            if(networkConnectionCheck==null){
                networkConnectionCheck= NetworkCallBack(applicationContext)
                networkConnectionCheck!!.register()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.i("NetworkService","서비스 onStartCommand()")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("NetworkService","서비스 onDetroy()")
    }

    /*fun getNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork : NetworkInfo? = cm.activeNetworkInfo
        val isConnected : Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected

    }

    fun checkNetworkState(): Boolean {
        val connectivityManager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }*/
}