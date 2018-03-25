package cz.vcelnicerudna

import android.os.Bundle

class EmailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        super.actionBarToggleWithNavigation(this)
    }
}
