package cz.vcelnicerudna.prices

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.PricesAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.configuration.StringConstants.Companion.RESERVATION_OK
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.data.model.Price
import cz.vcelnicerudna.reserve.ReserveActivity
import kotlinx.android.synthetic.main.activity_prices.*

class PricesActivity : BaseActivity(), PricesContract.ViewInterface {

    private val reservationActivityCode: Int = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PricesAdapter
    private lateinit var pricesPresenter: PricesContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prices)

        viewAdapter = PricesAdapter(this, listOf())
        pricesPresenter = PricesPresenter(this, RepositoryImpl(), appDatabase)

        recyclerView = prices_recycler_view.apply { adapter = viewAdapter }

        loadPrices()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == reservationActivityCode && resultCode == RESULT_OK) {
            val postOk = data?.getBooleanExtra(RESERVATION_OK, false)
            if (postOk != null && postOk == true) {
                getIndefiniteSnack(main_view_prices, R.string.reservation_sent_success, R.string.ok) {}.show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadPrices() {
        progress_bar.visibility = VISIBLE
        pricesPresenter.fetchPricesFromApi()
    }

    override fun onNetworkError() {
        pricesPresenter.fetchPricesFromLocalDataStore()
    }

    override fun showError() {
        progress_bar.visibility = GONE
        getIndefiniteSnack(main_view_prices, R.string.network_error, R.string.reload) { loadPrices() }.show()
    }

    override fun showPrices(prices: List<Price>) {
        if (prices.isEmpty()) {
            empty_message.visibility = VISIBLE
        } else {
            empty_message.visibility = GONE
            prices_recycler_view.visibility = VISIBLE
            viewAdapter.update(prices)
        }
    }

    override fun onReserveClicked(price: Price) {
        val intent = Intent(this, ReserveActivity::class.java)
        intent.putExtra(StringConstants.PRICE_KEY, price)
        startActivityForResult(intent, reservationActivityCode)
    }

    override fun onPricesLoaded() {
        progress_bar.visibility = GONE
    }
}
