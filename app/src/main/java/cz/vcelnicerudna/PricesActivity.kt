package cz.vcelnicerudna

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.adapters.PricesAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Price
import cz.vcelnicerudna.models.PricesData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_prices.*

class PricesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PricesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prices)
        super.actionBarToggleWithNavigation(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PricesAdapter(this, arrayOf())

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
        if (isConnectedToInternet()) {
            fetchPricesFromAPI()
        } else {
            fetchPricesFromDatabase()
        }
    }

    private fun fetchPricesFromAPI() {
        val compositeDisposable = CompositeDisposable()
        val disposable: Disposable = vcelniceAPI.getPrices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { prices: Array<Price> ->
                            onFetchSuccess(prices)
                            insertPricesToDatabase(prices)
                            compositeDisposable.dispose()
                        }
                ) {
                    onFetchError()
                    compositeDisposable.dispose()
                }
        compositeDisposable.add(disposable)
    }

    private fun fetchPricesFromDatabase() {
        appDatabaseWorkerThread.postTask(Runnable {
            val prices = appDatabase?.pricesDao()?.getPrices()
            if (prices == null) {
                onFetchError()
            } else {
                onFetchSuccess(prices.data)
            }
        })
    }

    private fun onFetchSuccess(prices: Array<Price>) {
        loading_content.visibility = View.GONE
        if (prices.isEmpty()) {
            empty_message.visibility = View.VISIBLE
        } else {
            empty_message.visibility = View.GONE
            prices_recycler_view.visibility = View.VISIBLE
            viewAdapter.loadNewData(prices)
        }
    }

    private fun onFetchError() {
        loading_content.visibility = View.GONE
        val snackbar = getThemedSnackBar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.reload)) {
            snackbar.dismiss()
            loadPrices()
        }
        snackbar.show()
    }

    private fun insertPricesToDatabase(prices: Array<Price>) {
        val pricesData = PricesData()
        pricesData.data = prices
        appDatabaseWorkerThread.postTask(Runnable { appDatabase?.pricesDao()?.insert(pricesData) })
    }
}
