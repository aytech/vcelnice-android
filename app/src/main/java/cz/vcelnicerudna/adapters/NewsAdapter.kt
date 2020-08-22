package cz.vcelnicerudna.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.news.NewsDetailActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.News

class NewsAdapter(var context: Context, private var newsDataSet: List<News>) :
        RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NewsViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_news, parent, false) as View
        return NewsViewHolder(textView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem: News = newsDataSet[position]
        holder.titleView.text = newsItem.title
        holder.descriptionView.text = newsItem.getParsedText()
        if (newsItem.icon != null) {
            Picasso
                    .get()
                    .load(APIConstants.VCELNICE_BASE_URL + newsItem.icon)
                    .placeholder(R.mipmap.ic_bee)
                    .into(holder.imageView)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(StringConstants.NEWS_KEY, newsItem)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = newsDataSet.size

    fun loadNewData(news: List<News>) {
        this.newsDataSet = news
        notifyDataSetChanged()
    }
}
