package cz.vcelnicerudna

import android.os.Bundle
import cz.vcelnicerudna.interfaces.VcelniceAPI

class NewsActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        super.actionBarToggleWithNavigation(this)
        loadNews()
    }

    fun loadNews() {

    }
}
