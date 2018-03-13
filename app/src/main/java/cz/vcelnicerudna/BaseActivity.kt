package cz.vcelnicerudna

import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setSupportActionBar(toolbar)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_photo -> {

            }
            R.id.nav_video -> {

            }
            R.id.nav_certificates -> {

            }
            R.id.nav_region -> {

            }
            R.id.nav_news -> {

            }
            R.id.nav_recipes -> {

            }
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
