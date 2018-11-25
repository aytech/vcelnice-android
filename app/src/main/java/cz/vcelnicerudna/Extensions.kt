package cz.vcelnicerudna

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.text.Html
import android.text.Spanned

fun Context.isConnectedToInternet(): Boolean {

    val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connManager.activeNetworkInfo ?: return false
    return activeNetwork.isConnected
}

fun loadHTML(html: String): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
}