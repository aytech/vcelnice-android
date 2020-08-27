package cz.vcelnicerudna.reserve

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.adapters.AdapterViewListener
import cz.vcelnicerudna.adapters.ArrayAdapterWithPlaceholder
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.databinding.ActivityReserveBinding
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.Price
import cz.vcelnicerudna.viewmodel.ReservationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reserve.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.net.URLEncoder

class ReserveActivity : BaseActivity(), ReserveContract.ViewInterface {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }
    private val classTag = ReserveActivity::class.simpleName
    private lateinit var spinnerArrayAdapter: ArrayAdapter<String>
    private lateinit var locationsArrayAdapter: ArrayAdapter<String>
    private var numberOfGlasses: Int = 0
    private var pickAddress: String = ""
    private lateinit var price: Price
    private lateinit var reservePresenter: ReservePresenter
    private val viewModel: ReservationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundledPrice: Price? = intent.getParcelableExtra(StringConstants.PRICE_KEY)
        if (bundledPrice == null) {
            finish()
        } else {
            price = bundledPrice
        }
        setContentView(R.layout.activity_reserve)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        reservePresenter = ReservePresenter(this, VcelniceAPI.create(), appDatabase)
        val binding = DataBindingUtil.setContentView<ActivityReserveBinding>(this, R.layout.activity_reserve)
        binding.viewModel = viewModel

         setNumberOfGlassesData()
        getLocations()

        // reserve_button.setOnClickListener {
        //    if (numberOfGlassesValid() && emailValid()) {
        //        email_error.visibility = View.INVISIBLE
        //        postReservation()
        //    }
        // }
    }

    private fun emailValid(): Boolean {
        if (TextUtils.isEmpty(email.text)
                || !Patterns.EMAIL_ADDRESS.matcher(email.text as CharSequence).matches()) {
            email_error.text = getString(R.string.enter_valid_email)
            email_error.visibility = View.VISIBLE
            return false
        }
        email_error.visibility = View.INVISIBLE
        return true
    }

    private fun numberOfGlassesValid(): Boolean {
        if (numberOfGlasses == 0) {
            number_error.text = getString(R.string.enter_number_of_glasses)
            number_error.visibility = View.VISIBLE
            return false
        }
        number_error.visibility = View.INVISIBLE
        return true
    }

    private fun setNumberOfGlassesData() {
        glasses_spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateGlassesCount(position)
            }
        }
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
        locationsArrayAdapter = ArrayAdapterWithPlaceholder(this, R.layout.spinner_item, locations.map { it.address })
        locationsArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        location.adapter = locationsArrayAdapter
        location.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                super.onItemSelected(parent, view, position, id)
                if (selectedData.isNotEmpty()) {
                    pickAddress = selectedData
                    selectedData = ""
                }
            }
        }
    }

    override fun onNetworkError() {
        reservePresenter.fetchLocationsFromLocalDataStore()
    }
}
