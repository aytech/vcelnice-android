@file:Suppress("DEPRECATION")

package cz.vcelnicerudna

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.text.Html
import android.text.Spanned

fun Context.isConnectedToInternet(): Boolean {
    var isConnected = false
    val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        connManager.run {
            connManager.getNetworkCapabilities(connManager.activeNetwork)?.run {
                isConnected = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        connManager.run {
            val networkInfo: NetworkInfo? = connManager.activeNetworkInfo
            connManager.activeNetworkInfo.run {
                if (networkInfo!!.type == ConnectivityManager.TYPE_WIFI) {
                    isConnected = true
                } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    isConnected = true
                }
            }
            return false
        }
    }
    return isConnected
}

fun loadHTML(html: String): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
}