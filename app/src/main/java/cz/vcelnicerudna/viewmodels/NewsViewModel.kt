package cz.vcelnicerudna.viewmodels

import android.text.Spanned

data class NewsViewModel(
        val title: String?,
        val text: Spanned?,
        val iconUrl: String?,
        val onClick: () -> Unit
)