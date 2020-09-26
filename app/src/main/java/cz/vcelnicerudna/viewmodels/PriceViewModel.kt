package cz.vcelnicerudna.viewmodels

data class PriceViewModel(
        val title: String?,
        val text: String?,
        val icon: String?,
        val onClick: () -> Unit
)