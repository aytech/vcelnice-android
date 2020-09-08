package cz.vcelnicerudna.reserve

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.AdapterViewListener
import cz.vcelnicerudna.configuration.StringConstants.Companion.PRICE_KEY
import cz.vcelnicerudna.configuration.StringConstants.Companion.RESERVATION_OK
import cz.vcelnicerudna.configuration.StringConstants.Companion.UTF_8
import cz.vcelnicerudna.data.PricesRepositoryImpl
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.databinding.ActivityReserveBinding
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.Price
import kotlinx.android.synthetic.main.activity_reserve.*
import java.net.URLEncoder

class ReserveActivity : BaseActivity(), ReserveContract.ViewInterface {

    private lateinit var price: Price
    private lateinit var reservePresenter: ReservePresenter
    private lateinit var viewModel: ReserveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set view binding
        val binding = DataBindingUtil.setContentView<ActivityReserveBinding>(this, R.layout.activity_reserve)
        viewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)
        binding.viewModel = viewModel
        // Set toolbar and back button
        setSupportActionBar(binding.reserveToolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Get price from parent activity
        val bundledPrice: Price? = intent.getParcelableExtra(PRICE_KEY)
        if (bundledPrice == null) {
            finish()
        } else {
            price = bundledPrice
        }
        // Init presenter
        reservePresenter = ReservePresenter(this, PricesRepositoryImpl(), appDatabase)

        updateSpinners()
        getLocations()
    }

    private fun updateSpinners() {
        glasses_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateGlassesCount(position)
            }
        }
        locations_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateLocation(position)
            }
        }
        reserve_button.setOnClickListener { postReservation() }
    }

    private fun getLocations() {
        reservePresenter.fetchLocationsFromApi()
    }

    override fun postReservation() {
        if (viewModel.canPostReservation()) {
            val reservation = Reservation(
                    price.title,
                    viewModel.glassesCount.get(),
                    viewModel.locationEntry.get(),
                    viewModel.email.get(),
                    URLEncoder.encode(viewModel.message.get(), UTF_8))
            reservePresenter.postReservation(reservation)
        }
    }

    override fun onFailPostReservation() {
        getThemedSnackBar(main_view, R.string.network_error, LENGTH_LONG).show()
    }

    override fun onSuccessPostReservation() {
        val intent = Intent()
        intent.putExtra(RESERVATION_OK, true)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun showLocations(locations: List<Location>?) {
        viewModel.updateLocations(locations)
    }

    override fun onNetworkError() {
        reservePresenter.fetchLocationsFromLocalDataStore()
    }
}
