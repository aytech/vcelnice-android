package cz.vcelnicerudna

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_reserve.*
import kotlinx.android.synthetic.main.app_toolbar.*

class ReserveActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)
        setSupportActionBar(app_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setNumberOfGlassesData()


        Log.d("ReserveActivity", "Price: " + intent.getParcelableExtra("price"))
    }

    private fun setNumberOfGlassesData() {
        val spinnerArrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, arrayOf(getString(R.string.number_of_glasses), "1", "2", "3")) {
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
                val selectedItemText = parent?.getItemAtPosition(position)
                if (position > 0) {
                    Snackbar.make(main_view, "Selected: $selectedItemText", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = onItemSelectedListener
    }
}
