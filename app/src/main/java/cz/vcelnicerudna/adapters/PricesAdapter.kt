package cz.vcelnicerudna.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cz.vcelnicerudna.databinding.FragmentPricesBinding
import cz.vcelnicerudna.models.Price
import cz.vcelnicerudna.prices.PricesContract
import cz.vcelnicerudna.viewmodels.PriceViewModel
import kotlinx.android.synthetic.main.fragment_prices.view.*

class PricesAdapter(private val activity: PricesContract.ViewInterface, private var prices: List<PriceViewModel>) :
        RecyclerView.Adapter<PricesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentPricesBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount() = prices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(price = prices[position])

    private fun showPriceDetail(price: Price) {
        activity.onReserveClicked(price)
    }

    fun update(prices: List<Price>) {
        this.prices = prices.map {
            PriceViewModel(
                    title = it.title,
                    text = it.getShortStringRepresentation(),
                    icon = it.image,
                    onClick = { showPriceDetail(it) }
            )
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FragmentPricesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(price: PriceViewModel) {
            binding.price = price
            binding.root.reserve_button.setOnClickListener { price.onClick() }
        }
    }
}