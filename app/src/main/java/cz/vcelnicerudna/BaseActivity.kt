package cz.vcelnicerudna

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import cz.vcelnicerudna.configuration.AppConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setSupportActionBar(app_toolbar)
    }

    fun actionBarToggleWithNavigation(activity: Activity) {
        val toggle = ActionBarDrawerToggle(
                activity,
                drawer_layout,
                app_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_fingerprint_white_24dp)
        toggle.setToolbarNavigationClickListener { drawer_layout.openDrawer(Gravity.START) }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.itemIconTintList = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.nav_news -> {
                startActivity(Intent(this, NewsActivity::class.java))
            }
            R.id.nav_prices -> {
                startActivity(Intent(this, PricesActivity::class.java))
            }
            R.id.nav_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel: %s".format(AppConstants.CONTACT_PHONE))
                startActivity(intent)
            }
            R.id.nav_email -> {
                startActivity(Intent(this, EmailActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
