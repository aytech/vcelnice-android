package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cz.vcelnicerudna.adapters.PricesAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
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
        vcelniceAPI.getPrices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            loading_content.visibility = View.GONE
                            if (result.isEmpty()) {
                                empty_message.visibility = View.VISIBLE
                            } else {
                                empty_message.visibility = View.GONE
                                prices_recycler_view.visibility = View.VISIBLE
                                viewAdapter.loadNewData(result)
                            }
                        },
                        {
                            loading_content.visibility = View.GONE
                            val snackbar = getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                            snackbar.setAction(getString(R.string.reload), {
                                snackbar.dismiss()
                                loadPrices()
                            })
                            snackbar.show()
                        }
                )
    }
}
