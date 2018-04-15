package cz.vcelnicerudna

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Photo
import kotlinx.android.synthetic.main.activity_photo_view.*
import kotlinx.android.synthetic.main.app_toolbar.*

class PhotoViewActivity : AppCompatActivity() {

    private lateinit var photoItem: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        photoItem = intent.getParcelableExtra(StringConstants.PHOTO_KEY)

        title = photoItem.caption
        GlideApp
                .with(this)
                .load(APIConstants.VCELNICE_BASE_URL + photoItem.image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        loading_content.visibility = View.GONE
                        return false
                    }
                })
                .placeholder(R.mipmap.ic_bee)
                .fitCenter()
                .into(image)
    }
}
