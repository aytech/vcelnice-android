package cz.vcelnicerudna

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reserve.*

class ReserveActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)
        setSupportActionBar(app_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
