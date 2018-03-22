package cz.vcelnicerudna.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.vcelnicerudna.R
import cz.vcelnicerudna.models.News

class NewsAdapter(private var newsDataSet: Array<News>) :
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
        Log.d("NewsAdapter", "Loading data: $textView")
        return NewsViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textView.text = newsDataSet[position].text
        Log.d("NewsAdapter", "Loading data: $holder")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = newsDataSet.size

    fun loadNewData(news: Array<News>) {
        this.newsDataSet = news
        notifyDataSetChanged()
    }
}
