package cz.vcelnicerudna.reserve

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.RoundedCornersTransformation
import cz.vcelnicerudna.adapters.AdapterViewListener
import cz.vcelnicerudna.configuration.APIConstants.Companion.VCELNICE_BASE_URL
import cz.vcelnicerudna.configuration.StringConstants.Companion.PRICE_KEY
import cz.vcelnicerudna.configuration.StringConstants.Companion.RESERVATION_OK
import cz.vcelnicerudna.configuration.StringConstants.Companion.UTF_8
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.databinding.ActivityReserveBinding
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.Price
import kotlinx.android.synthetic.main.activity_reserve.*
import java.net.URLEncoder

class ReserveActivity : BaseActivity(), ReserveContract.ViewInterface {

    private lateinit var reservePresenter: ReservePresenter
    private lateinit var viewModel: ReserveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set view binding
        val binding = DataBindingUtil.setContentView<ActivityReserveBinding>(this, R.layout.activity_reserve)
        viewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)
        binding.viewModel = viewModel

        reservePresenter = ReservePresenter(this, RepositoryImpl(), appDatabase)

        bottom_app_bar_reserve.setNavigationOnClickListener { navigateHome() }
        bottom_app_bar_reserve.setOnMenuItemClickListener { onNavigationItemSelected(it, null) }

        loadImage()
        addListeners()
        getLocations()
    }

    private fun loadImage() {
        val bundledPrice: Price? = intent.getParcelableExtra(PRICE_KEY)
        if (bundledPrice == null) {
            finish()
        } else {
            viewModel.reservationTitle = bundledPrice.title.toString()
            if (bundledPrice.image != null) {
                Picasso
                        .get()
                        .load(VCELNICE_BASE_URL + bundledPrice.image)
                        .transform(RoundedCornersTransformation())
                        .placeholder(R.mipmap.ic_bee)
                        .into(price_image)
            }
        }
    }

    private fun addListeners() {
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
                    viewModel.reservationTitle,
                    viewModel.glassesCount.get(),
                    viewModel.locationEntry.get(),
                    viewModel.email.get(),
                    URLEncoder.encode(viewModel.message.get(), UTF_8))
            reservePresenter.postReservation(reservation)
        } else {
            getThemedSnackBar(main_view, viewModel.validationMessage, LENGTH_LONG).show()
        }
    }

    override fun showDefaultLocation() {
        viewModel.updateLocations(listOf(Location.default()))
    }

    override fun loadingComplete() {
        // Not yet implemented
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
