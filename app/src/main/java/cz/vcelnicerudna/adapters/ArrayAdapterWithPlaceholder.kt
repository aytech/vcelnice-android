package cz.vcelnicerudna.adapters

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cz.vcelnicerudna.R

class ArrayAdapterWithPlaceholder(context: Context?, resource: Int, data: Array<String>)
    : ArrayAdapter<String>(context, resource, data) {

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View? = super.getDropDownView(position, convertView, parent)
        val textView: TextView = view as TextView
        if (position == 0) {
            textView.setTextColor(Color.GRAY)
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorText))
        }
        return view
    }
}