package cz.vcelnicerudna.viewmodels

import android.text.Spanned
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.models.News
import timber.log.Timber

data class MainViewModel(
        val title: String?,
        val text: Spanned?,
        val icon: String?
)