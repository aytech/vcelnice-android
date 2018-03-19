package cz.vcelnicerudna

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import cz.vcelnicerudna.interfaces.VcelniceAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class NewsActivity: BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.itemIconTintList = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
//            R.id.nav_photo -> {
//
//            }
//            R.id.nav_video -> {
//
//            }
//            R.id.nav_certificates -> {
//
//            }
//            R.id.nav_region -> {
//
//            }
            R.id.nav_news -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
            }
//            R.id.nav_recipes -> {
//
//            }
            R.id.nav_prices -> {

            }
            R.id.nav_call -> {

            }
            R.id.nav_email -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
