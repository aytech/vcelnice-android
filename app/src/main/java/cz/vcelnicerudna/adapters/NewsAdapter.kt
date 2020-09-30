package cz.vcelnicerudna.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cz.vcelnicerudna.configuration.StringConstants.Companion.NEWS_KEY
import cz.vcelnicerudna.databinding.FragmentNewsBinding
import cz.vcelnicerudna.data.model.News
import cz.vcelnicerudna.news.NewsDetailActivity
import cz.vcelnicerudna.data.viewmodels.NewsViewModel

class NewsAdapter(private var news: List<NewsViewModel>) :
        RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = FragmentNewsBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(news = news[position])

    override fun getItemCount() = news.size

    private fun navigateToDetail(news: News) {
        val intent = Intent(context, NewsDetailActivity::class.java)
        intent.putExtra(NEWS_KEY, news)
        context.startActivity(intent)
    }

    fun update(news: List<News>) {
        this.news = news.map {
            NewsViewModel(
                    title = it.title,
                    text = it.getParsedText(),
                    iconUrl = it.icon,
                    onClick = { navigateToDetail(it) })
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: FragmentNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsViewModel) {
            binding.news = news
            binding.root.setOnClickListener { news.onClick() }
        }
    }
}