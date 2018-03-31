package cz.vcelnicerudna

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.design.R.id.snackbar_text
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import cz.vcelnicerudna.configuration.AppConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

    fun getThemedSnackbar(view: View, message: Int, length: Int): Snackbar {
        val snackbar = Snackbar.make(view, getString(message), length)
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        // Set text color
        val snackbarView: View = snackbar.view
        val snackBarTextView: TextView = snackbarView.findViewById(snackbar_text)
        snackBarTextView.setTextColor(ContextCompat.getColor(this, R.color.white))

        return snackbar
    }
}
