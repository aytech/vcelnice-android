package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import cz.vcelnicerudna.adapters.AdapterViewListener
import cz.vcelnicerudna.adapters.ArrayAdapterWithPlaceholder
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Location
import cz.vcelnicerudna.models.Price
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reserve.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.net.URLEncoder

class ReserveActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }
    private lateinit var spinnerArrayAdapter: ArrayAdapter<String>
    private lateinit var locationsArrayAdapter: ArrayAdapter<String>
    private var numberOfGlasses: Int = 0
    private var pickAddress: String = ""
    private lateinit var price: Price

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setNumberOfGlassesData()
        getLocations()
        price = intent.getParcelableExtra(StringConstants.PRICE_KEY)

        reserve_button.setOnClickListener { _ ->
            if (numberOfGlassesValid() && emailValid()) {
                email_error.visibility = View.INVISIBLE
                postReservation()
            }
        }
    }

    private fun emailValid(): Boolean {
        if (TextUtils.isEmpty(email.text)
                || !Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
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
        spinnerArrayAdapter = ArrayAdapterWithPlaceholder(this, R.layout.spinner_item, resources.getStringArray(R.array.glasses_array))
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = object : AdapterViewListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                super.onItemSelected(parent, view, position, id)
                if (selectedData.isNotEmpty()) {
                    numberOfGlasses = selectedData.toInt()
                    selectedData = ""
                }
            }
        }
    }

    private fun setLocationsData(data: Array<String>) {
        locationsArrayAdapter = ArrayAdapterWithPlaceholder(this, R.layout.spinner_item, data)
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

    private fun getLocations() {
        vcelniceAPI.getLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response: Array<Location> ->
                    val locations = ArrayList<String>()
                    locations.add(0, getString(R.string.pickup_at_address))
                    response.forEach { locations.add(it.address) }
                    setLocationsData(locations.toTypedArray())
                }
    }

    private fun postReservation() {
        val emailParam: String = email.text.toString()
        val messageParam: String = URLEncoder.encode(message.text.toString(), StringConstants.UTF_8)
        val titleParam = price.getStringRepresentation()
        vcelniceAPI.reserve(numberOfGlasses, emailParam, messageParam, titleParam, pickAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            numberOfGlasses = 0
                            spinner.adapter = spinnerArrayAdapter
                            email.text.clear()
                            message.text.clear()
                            getThemedSnackbar(main_view, R.string.reservation_sent_success, Snackbar.LENGTH_LONG)
                                    .show()
                        },
                        {
                            getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_LONG)
                                    .show()
                        }
                )
    }
}
