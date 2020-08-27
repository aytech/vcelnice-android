package cz.vcelnicerudna.reserve

import android.widget.Spinner
import androidx.databinding.BindingAdapter

class SpinnerBindings {
    interface ItemSelectedListener {
        fun onItemSelected(item: Any)
    }

    @BindingAdapter("onItemSelected")
    fun Spinner.setOnItemSelectedListener(itemSelectedListener: ItemSelectedListener) {

    }
}