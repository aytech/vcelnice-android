package cz.vcelnicerudna

import android.os.Bundle

class PricesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prices)
        super.actionBarToggleWithNavigation(this)
    }
}
