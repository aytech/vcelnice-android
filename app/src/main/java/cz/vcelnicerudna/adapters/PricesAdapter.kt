package cz.vcelnicerudna.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.R
import cz.vcelnicerudna.reserve.ReserveActivity
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Price

class PricesAdapter(var context: Context, private var dataSet: List<Price>) :
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
        holder.descriptionView.text = item.getShortStringRepresentation()
        if (item.image != null) {
            Picasso
                    .get()
                    .load(APIConstants.VCELNICE_BASE_URL + item.image)
                    .placeholder(R.mipmap.ic_bee)
                    .into(holder.imageView)
        }
        holder.button.setOnClickListener {
            val intent = Intent(context, ReserveActivity::class.java)
            intent.putExtra(StringConstants.PRICE_KEY, item)
            context.startActivity(intent)
        }
    }

    fun loadNewData(prices: List<Price>) {
        this.dataSet = prices
        notifyDataSetChanged()
    }
}