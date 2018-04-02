package cz.vcelnicerudna

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Price
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reserve.*
import kotlinx.android.synthetic.main.app_toolbar.*

class ReserveActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }
    private lateinit var spinnerArrayAdapter: ArrayAdapter<String>
    private var numberOfGlasses: Int = 0
    private lateinit var price: Price

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)
        setSupportActionBar(app_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setNumberOfGlassesData()
        price = intent.getParcelableExtra("price")

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
        spinnerArrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.glasses_array)) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view: View? = super.getDropDownView(position, convertView, parent)
                val textView: TextView = view as TextView
                if (position == 0) {
                    textView.setTextColor(Color.GRAY)
                } else {
                    textView.setTextColor(ContextCompat.getColor(this@ReserveActivity, R.color.colorText))
                }
                return view
            }
        }
        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItemText: String = parent?.getItemAtPosition(position).toString()
                if (position > 0) {
                    numberOfGlasses = selectedItemText.toInt()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun postReservation() {
        val emailParam = email.text.toString()
        val messageParam = message.text.toString()
        val titleParam = price.getStringRepresentation()
        vcelniceAPI.reserve(numberOfGlasses, emailParam, messageParam, titleParam)
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
