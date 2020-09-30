package cz.vcelnicerudna.news

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.databinding.ActivityNewsDetailBinding
import cz.vcelnicerudna.data.model.News
import cz.vcelnicerudna.data.viewmodels.NewsDetailViewModel

class NewsDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)
        binding.news = NewsDetailViewModel(null, null, null)

        val news = intent.getParcelableExtra<News>(StringConstants.NEWS_KEY)
        if (news == null) {
            finish()
        } else {
            binding.news = NewsDetailViewModel(news.title, news.text, news.icon)
        }
    }
}
