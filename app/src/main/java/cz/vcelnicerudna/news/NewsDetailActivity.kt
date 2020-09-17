package cz.vcelnicerudna.news

import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.News
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        action_call.setOnClickListener { handleCallAction() }

        val news = intent.getParcelableExtra<News>(StringConstants.NEWS_KEY)
        if (news == null) {
            finish()
        } else {
            Picasso
                    .get()
                    .load(APIConstants.VCELNICE_BASE_URL + news.icon)
                    .placeholder(R.mipmap.ic_bee)
                    .into(news_image as ImageView)
            news_title.text = news.title
            news_text.text = news.getParsedText()
        }
    }
}
