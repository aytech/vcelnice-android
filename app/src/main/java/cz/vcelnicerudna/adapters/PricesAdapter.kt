package cz.vcelnicerudna.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.R
import cz.vcelnicerudna.RoundedCornersTransformation
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.Price
import cz.vcelnicerudna.prices.PricesContract

class PricesAdapter(private var context: PricesContract.ViewInterface, private var dataSet: List<Price>) :
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
                    .transform(RoundedCornersTransformation())
                    .placeholder(R.mipmap.ic_bee)
                    .into(holder.imageView)
        }
        holder.button.setOnClickListener {
            context.onReserveClicked(item)
        }
    }

    fun loadNewData(prices: List<Price>) {
        this.dataSet = prices
        notifyDataSetChanged()
    }
}