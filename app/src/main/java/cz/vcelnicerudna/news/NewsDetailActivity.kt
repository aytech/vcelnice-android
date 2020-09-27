package cz.vcelnicerudna.news

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.databinding.ActivityNewsDetailBinding
import cz.vcelnicerudna.models.News
import cz.vcelnicerudna.viewmodels.NewsDetailViewModel
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)
        binding.news = NewsDetailViewModel(null, null, null)


        action_call.setOnClickListener { handleCallAction() }

        val news = intent.getParcelableExtra<News>(StringConstants.NEWS_KEY)
        if (news == null) {
            finish()
        } else {
            binding.news = NewsDetailViewModel(news.title, news.text, news.icon)
        }
    }
}
