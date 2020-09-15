package cz.vcelnicerudna

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.vcelnicerudna.contact.ContactActivity
import cz.vcelnicerudna.main.MainActivity
import cz.vcelnicerudna.photo.PhotoActivity
import cz.vcelnicerudna.prices.PricesActivity
import kotlinx.android.synthetic.main.bottom_bar.*

open class BaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    protected lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_menu.setOnNavigationItemSelectedListener(this)
    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent: Intent = when (item.itemId) {
            R.id.photo_page -> {
                Intent(this, PhotoActivity::class.java)
            }
            R.id.contact_page -> {
                Intent(this, ContactActivity::class.java)
            }
            R.id.prices_page -> {
                Intent(this, PricesActivity::class.java)
            }
            else -> {
                Intent(this, MainActivity::class.java)
            }
        }
        startActivity(intent)
        return true
//        when (item.itemId) {
//            R.id.nav_prices -> {
//                val intent = Intent(this, PricesActivity::class.java)
//                intent.flags = FLAG_ACTIVITY_REORDER_TO_FRONT
//                startActivity(intent)
//            }
//            R.id.nav_call -> {
//                val intent = Intent(Intent.ACTION_DIAL)
//                intent.data = Uri.parse("tel: %s".format(AppConstants.CONTACT_PHONE))
//                startActivity(intent)
//            }
//            R.id.nav_email -> {
//                val intent = Intent(this, EmailActivity::class.java)
//                intent.flags = FLAG_ACTIVITY_REORDER_TO_FRONT
//                startActivity(intent)
//            }
//        }
    }

    fun getThemedSnackBar(view: View, message: Int, length: Int): Snackbar {
        val snackBar = Snackbar.make(view, getString(message), length)
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val snackBarView: View = snackBar.view
        val snackBarTextView: TextView = snackBarView.findViewById(R.id.snackbar_text)
        snackBarTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
        return snackBar
    }
}
