package cz.vcelnicerudna

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import cz.vcelnicerudna.configuration.AppConstants.Companion.CONTACT_PHONE
import cz.vcelnicerudna.contact.ContactActivity
import cz.vcelnicerudna.main.MainActivity
import cz.vcelnicerudna.photo.PhotoActivity
import cz.vcelnicerudna.prices.PricesActivity

open class BaseActivity : AppCompatActivity() {

    protected lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(this)
    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        super.onDestroy()
    }

    fun navigateHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onNavigationItemSelected(item: MenuItem, currentItem: Int?): Boolean {
        if (item.itemId == currentItem) {
            return true
        }
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
    }

    fun handleCallAction() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: %s".format(CONTACT_PHONE))
        startActivity(intent)
    }

    private fun getSnack(view: CoordinatorLayout, fab: FloatingActionButton, message: Int, duration: Int): Snackbar {
        val snackBar = Snackbar.make(view, getString(message), duration)
        snackBar.anchorView = fab
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val snackBarView: View = snackBar.view
        val snackBarTextView: TextView = snackBarView.findViewById(R.id.snackbar_text)
        snackBarTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
        return snackBar
    }

    fun getLongSnack(view: CoordinatorLayout, fab: FloatingActionButton, message: Int): Snackbar {
        val snack = getSnack(view, fab, message, LENGTH_LONG)
        snack.setAction(getString(R.string.ok)) { snack.dismiss() }
        return snack
    }

    fun getIndefiniteSnack(view: CoordinatorLayout, fab: FloatingActionButton, message: Int, action: Int, onAction: () -> Unit): Snackbar {
        val snack = getSnack(view, fab, message, LENGTH_INDEFINITE)
        snack.setAction(getString(action)) {
            snack.dismiss()
            onAction()
        }
        return snack
    }

    fun isKeyboardOpen(view: View): Boolean {
        val heightDifference: Int = view.rootView.height - view.height
        return heightDifference > .25 * view.rootView.height
    }

    fun setPromotedFabAction(fab: FloatingActionButton, drawable: Int, action: () -> Unit) {
        val timer = object : CountDownTimer(100, 100) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, drawable))
                fab.setOnClickListener { action() }
            }

        }
        timer.start()
    }
}
