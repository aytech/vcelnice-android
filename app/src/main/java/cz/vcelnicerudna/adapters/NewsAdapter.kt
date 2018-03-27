package cz.vcelnicerudna.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.GlideApp
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.News

class NewsAdapter(var context: Context, private var newsDataSet: Array<News>) :
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
        holder.descriptionView.text = Html.fromHtml(newsItem.text)
        if (newsItem.icon != null) {
            GlideApp
                    .with(context)
                    .load(APIConstants.VCELNICE_BASE_URL + newsItem.icon)
                    .placeholder(R.mipmap.ic_bee)
                    .fitCenter()
                    .into(holder.imageView)

        }
    }

    override fun getItemCount() = newsDataSet.size

    fun loadNewData(news: Array<News>) {
        this.newsDataSet = news
        notifyDataSetChanged()
    }
}
