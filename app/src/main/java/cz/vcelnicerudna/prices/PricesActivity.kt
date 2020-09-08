package cz.vcelnicerudna.prices

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.PricesAdapter
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.configuration.StringConstants.Companion.RESERVATION_OK
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Price
import cz.vcelnicerudna.reserve.ReserveActivity
import kotlinx.android.synthetic.main.content_prices.*

class PricesActivity : BaseActivity(), PricesContract.ViewInterface {

    private val reservationActivityCode: Int = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PricesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var pricesPresenter: PricesContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prices)
        super.actionBarToggleWithNavigation(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PricesAdapter(this, listOf())
        pricesPresenter = PricesPresenter(this, VcelniceAPI.create(), appDatabase)

        recyclerView = findViewById<RecyclerView>(R.id.prices_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
        loadPrices()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == reservationActivityCode && resultCode == RESULT_OK) {
            val postOk = data?.getBooleanExtra(RESERVATION_OK, false)
            if (postOk != null && postOk == true) {
                getThemedSnackBar(main_view, R.string.reservation_sent_success, LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadPrices() {
        loading_content.visibility = View.VISIBLE
        pricesPresenter.fetchPricesFromApi()
    }

    override fun onNetworkError() {
        pricesPresenter.fetchPricesFromLocalDataStore()
    }

    override fun showError() {
        loading_content.visibility = View.GONE
        val snackBar = getThemedSnackBar(main_view, R.string.network_error, LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.reload)) {
            snackBar.dismiss()
            loadPrices()
        }
        snackBar.show()
    }

    override fun showPrices(prices: List<Price>) {
        loading_content.visibility = View.GONE
        if (prices.isEmpty()) {
            empty_message.visibility = View.VISIBLE
        } else {
            empty_message.visibility = View.GONE
            prices_recycler_view.visibility = View.VISIBLE
            viewAdapter.loadNewData(prices)
        }
    }

    override fun onReserveClicked(price: Price) {
        val intent = Intent(this, ReserveActivity::class.java)
        intent.putExtra(StringConstants.PRICE_KEY, price)
        startActivityForResult(intent, reservationActivityCode)
    }
}
