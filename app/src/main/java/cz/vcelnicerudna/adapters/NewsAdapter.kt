package cz.vcelnicerudna.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import cz.vcelnicerudna.R
import cz.vcelnicerudna.models.News

class NewsAdapter(private val newsDataSet: Array<News>) :
        RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var news: Array<News>

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NewsAdapter.ViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_news, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = newsDataSet[position].text
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = newsDataSet.size

    fun loadNewData(news: Array<News>) {
        this.news = news
        notifyDataSetChanged()
    }
}
