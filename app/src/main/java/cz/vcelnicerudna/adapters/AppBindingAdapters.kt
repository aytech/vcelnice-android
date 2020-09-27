package cz.vcelnicerudna.adapters

import android.webkit.WebView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) {
        Picasso.get()
                .load(APIConstants.VCELNICE_BASE_URL + url)
                .placeholder(R.mipmap.ic_bee)
                .error(R.mipmap.ic_bee)
                .into(view)
    }
}

@BindingAdapter("webContent")
fun loadContentIntoWebView(webView: WebView, content: String) {
    webView.loadData(content, "text/html", "utf-8")
}