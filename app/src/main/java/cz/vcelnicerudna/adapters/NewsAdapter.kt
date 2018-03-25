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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
//    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NewsViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_news, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        return NewsViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = newsDataSet.size

    fun loadNewData(news: Array<News>) {
        this.newsDataSet = news
        notifyDataSetChanged()
    }
}
