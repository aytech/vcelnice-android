package cz.vcelnicerudna.prices

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.PricesAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Price
import kotlinx.android.synthetic.main.content_prices.*

class PricesActivity : BaseActivity(), PricesContract.ViewInterface {

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

    private fun loadPrices() {
        loading_content.visibility = View.VISIBLE
        pricesPresenter.fetchPricesFromApi()
    }

    override fun onNetworkError() {
        pricesPresenter.fetchPricesFromLocalDataStore()
    }

    override fun showError() {
        loading_content.visibility = View.GONE
        val snackBar = getThemedSnackBar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
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
}
