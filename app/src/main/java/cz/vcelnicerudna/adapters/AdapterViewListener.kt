package cz.vcelnicerudna.adapters

import android.view.View
import android.widget.AdapterView

open class AdapterViewListener : AdapterView.OnItemSelectedListener {

    protected var selectedData: String = ""

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Nothing to do here
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItemText: String = parent?.getItemAtPosition(position).toString()
        if (position > 0) {
            selectedData = selectedItemText
        }
    }

}