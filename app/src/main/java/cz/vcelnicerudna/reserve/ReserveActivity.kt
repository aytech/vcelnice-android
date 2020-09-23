package cz.vcelnicerudna.reserve

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.AdapterViewListener
import cz.vcelnicerudna.configuration.StringConstants.Companion.PRICE_KEY
import cz.vcelnicerudna.configuration.StringConstants.Companion.RESERVATION_OK
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.databinding.ActivityReserveBinding
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.Price
import kotlinx.android.synthetic.main.activity_reserve.*
import timber.log.Timber

class ReserveActivity : BaseActivity(), ReserveContract.ViewInterface {

    private lateinit var reservePresenter: ReservePresenter
    private lateinit var viewModel: ReserveViewModel
    private val fabActionChangeListener: ViewTreeObserver.OnGlobalLayoutListener
        get() = ViewTreeObserver.OnGlobalLayoutListener {
            if (isKeyboardOpen(main_view_reserve)) {
                setPromotedFabAction(action_call_reserve, R.drawable.ic_paper_plane) { postReservation() }
            } else {
                setPromotedFabAction(action_call_reserve, R.drawable.ic_baseline_phone_in_talk_30) { handleCallAction() }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        val binding = DataBindingUtil.setContentView<ActivityReserveBinding>(this, R.layout.activity_reserve)
        viewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)
        binding.viewModel = viewModel

        reservePresenter = ReservePresenter(this, RepositoryImpl(), appDatabase)

        addListeners()
        loadImage()
        getLocations()
    }

    private fun loadImage() {
        val bundledPrice: Price? = intent.getParcelableExtra(PRICE_KEY)
        if (bundledPrice == null) {
            finish()
        } else {
            viewModel.reservationTitle = bundledPrice.title.toString()
            if (bundledPrice.image != null) {
                loadImageIntoView(price_image, bundledPrice.image!!)
            }
        }
    }

    private fun addListeners() {
        // Number of glasses selector
        glasses_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateGlassesCount(position)
            }
        }
        // Location selector
        locations_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateLocation(position)
            }
        }
        // Reservation button
        reserve_button.setOnClickListener { postReservation() }
        // Promoted "call" FAB
        action_call_reserve.setOnClickListener { handleCallAction() }
        // Home button
        bottom_app_bar_reserve.setNavigationOnClickListener { navigateHome() }
        // Navigation
        bottom_app_bar_reserve.setOnMenuItemClickListener { onNavigationItemSelected(it, null) }
        // Keyboard detector
        main_view_reserve.viewTreeObserver.addOnGlobalLayoutListener(fabActionChangeListener)
    }

    private fun getLocations() {
        reservePresenter.fetchLocationsFromApi()
    }

    override fun postReservation() {
        if (viewModel.canPostReservation()) {
            reservePresenter.postReservation(viewModel.getReservation())
        } else {
            getLongSnack(main_view_reserve, action_call_reserve, viewModel.validationMessage).show()
        }
    }

    override fun showDefaultLocation() {
        viewModel.updateLocations(listOf(Location.default()))
    }

    override fun onLocationsFetchComplete() {
        Timber.d("Finished loading locations from API")
    }

    override fun onFailPostReservation(error: Throwable) {
        Timber.d("Error posting reservation to API: $error")
        getLongSnack(main_view_reserve, action_call_reserve, R.string.network_error).show()
    }

    override fun onSuccessPostReservation() {
        val intent = Intent()
        intent.putExtra(RESERVATION_OK, true)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCompletePostReservation() {
        Timber.d("Finished posting reservation to API")
    }

    override fun showLocations(locations: List<Location>?) {
        viewModel.updateLocations(locations)
    }

    override fun onNetworkError() {
        reservePresenter.fetchLocationsFromLocalDataStore()
    }
}
