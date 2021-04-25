package com.privatememo.j.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.privatememo.j.utility.Utility


class NetworkCallBack : ConnectivityManager.NetworkCallback {

    var context: Context? = null
    var networkRequest: NetworkRequest? = null
    var connectivityManager: ConnectivityManager? = null

    constructor(context: Context?) {
        this.context = context
        networkRequest = NetworkRequest.Builder() // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
            .build()
        connectivityManager =
            this.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager // CONNECTIVITY_SERVICE : 네트워크 연결 관리 처리를 검색
    }

    fun register() {
        connectivityManager!!.registerNetworkCallback(networkRequest!!, this)
    }

    fun unregister() {
        connectivityManager!!.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        // 네트워크가 연결되었을 때 할 동작
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        // 네트워크 연결이 끊겼을 때 할 동작

        /*var view1 = LayoutInflater.from(context).inflate(R.layout.networkcustomdialog,null)

        var toast = Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0); // CENTER를 기준으로 0, 0 위치에 메시지 출력
        toast.setDuration(Toast.LENGTH_LONG);
        toast.view = view1
        toast.show()*/

        Utility.NetworkState.off = true
    }
}