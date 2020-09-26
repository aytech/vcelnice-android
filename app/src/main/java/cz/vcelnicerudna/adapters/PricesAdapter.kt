package cz.vcelnicerudna.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cz.vcelnicerudna.databinding.FragmentPricesBinding
import cz.vcelnicerudna.models.Price
import cz.vcelnicerudna.viewmodels.PricesViewModel
import timber.log.Timber

class PricesAdapter(private var prices: List<PricesViewModel>) :
        RecyclerView.Adapter<PricesAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = FragmentPricesBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount() = prices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(price = prices[position])

//    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
//        val item: Price = dataSet[position]
//        holder.titleView.text = item.title
//        holder.descriptionView.text = item.getShortStringRepresentation()
//        if (item.image != null) {
//            Picasso
//                    .get()
//                    .load(APIConstants.VCELNICE_BASE_URL + item.image)
//                    .transform(RoundedCornersTransformation())
//                    .placeholder(R.mipmap.ic_bee)
//                    .into(holder.imageView)
//        }
//        holder.button.setOnClickListener {
//            // context.onReserveClicked(item)
//        }
//    }

    fun update(prices: List<Price>) {
        Timber.d("Loading prices: %s", prices)
        //this.dataSet = prices
        this.prices = prices.map {
            PricesViewModel(
                    title = it.title,
                    text = it.getShortStringRepresentation(),
                    icon = it.image
            )
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FragmentPricesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(price: PricesViewModel) {
            binding.price = price
        }
    }
}