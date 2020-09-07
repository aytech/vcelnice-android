package cz.vcelnicerudna.reserve

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.AdapterViewListener
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.databinding.ActivityReserveBinding
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.Price
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reserve.*
import java.net.URLEncoder

class ReserveActivity : BaseActivity(), ReserveContract.ViewInterface {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }
    private lateinit var spinnerArrayAdapter: ArrayAdapter<String>
    private var numberOfGlasses: Int = 0
    private var pickAddress: String = ""
    private lateinit var price: Price
    private lateinit var reservePresenter: ReservePresenter
    private lateinit var viewModel: ReserveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set view binding
        val binding = DataBindingUtil.setContentView<ActivityReserveBinding>(this, R.layout.activity_reserve)
        viewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)
        binding.viewModel = viewModel

        setSupportActionBar(binding.reserveToolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundledPrice: Price? = intent.getParcelableExtra(StringConstants.PRICE_KEY)
        if (bundledPrice == null) {
            finish()
        } else {
            price = bundledPrice
        }
        // Init presenter
        reservePresenter = ReservePresenter(this, VcelniceAPI.create(), appDatabase)
        // Set glasses count adapter
        glasses_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateGlassesCount(position)
            }
        }
        // Set locations adapter
        locations_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateLocation(position)
            }
        }
        getLocations()
    }

    private fun getLocations() {
        reservePresenter.fetchLocationsFromAPI()
    }

    private fun postReservation() {
        val emailParam: String = email.text.toString()
        val messageParam: String = URLEncoder.encode(message.text.toString(), StringConstants.UTF_8)
        val titleParam = price.getStringRepresentation()
        val compositeDisposable = CompositeDisposable()
        val disposable: Disposable = vcelniceAPI.reserve(numberOfGlasses, emailParam, messageParam, titleParam, pickAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            numberOfGlasses = 0
                            glasses_spinner.adapter = spinnerArrayAdapter
                            email.text?.clear()
                            message.text?.clear()
                            getThemedSnackBar(main_view, R.string.reservation_sent_success, Snackbar.LENGTH_LONG)
                                    .show()
                            compositeDisposable.dispose()
                        },
                        {
                            getThemedSnackBar(main_view, R.string.network_error, Snackbar.LENGTH_LONG)
                                    .show()
                            compositeDisposable.dispose()
                        }
                )
        compositeDisposable.add(disposable)
    }

    override fun showLocations(locations: List<Location>) {
        viewModel.updateLocations(locations)
    }

    override fun onNetworkError() {
        reservePresenter.fetchLocationsFromLocalDataStore()
    }
}
