package cz.vcelnicerudna.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.GlideApp
import cz.vcelnicerudna.R
import cz.vcelnicerudna.ReserveActivity
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.Price

class PricesAdapter(var context: Context, private var dataSet: Array<Price>) :
        RecyclerView.Adapter<PriceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_prices, parent, false) as View
        return PriceViewHolder(textView)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val item: Price = dataSet[position]
        holder.titleView.text = item.title
        holder.descriptionView.text = "%s Kč (%s)".format(item.price, item.weight)
        if (item.image != null) {
            GlideApp
                    .with(context)
                    .load(APIConstants.VCELNICE_BASE_URL + item.image)
                    .placeholder(R.mipmap.ic_bee)
                    .fitCenter()
                    .into(holder.imageView)
        }
        holder.button.setOnClickListener { view ->
            val intent = Intent(context, ReserveActivity::class.java)
            intent.putExtra("price", item)
            context.startActivity(intent)
        }
    }

    fun loadNewData(prices: Array<Price>) {
        this.dataSet = prices
        notifyDataSetChanged()
    }
}