package cz.vcelnicerudna

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        GlideApp
                .with(this)
                .load(APIConstants.VCELNICE_BASE_URL + newsItem.icon)
                .placeholder(R.mipmap.ic_bee)
                .fitCenter()
                .into(news_image)
        news_title.text = newsItem.title
        news_text.text = newsItem.getParsedText()
    }
}
