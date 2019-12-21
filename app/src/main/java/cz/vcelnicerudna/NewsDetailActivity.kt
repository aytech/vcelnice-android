package cz.vcelnicerudna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.News
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.app_toolbar.*

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var newsItem: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        newsItem = intent.getParcelableExtra(StringConstants.NEWS_KEY)

        Picasso
                .with(this)
                .load(APIConstants.VCELNICE_BASE_URL + newsItem.icon)
                .placeholder(R.mipmap.ic_bee)
                .into(news_image as ImageView)
        news_title.text = newsItem.title
        news_text.text = newsItem.getParsedText()
    }
}
