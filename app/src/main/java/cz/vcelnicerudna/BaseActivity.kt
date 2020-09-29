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
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import cz.vcelnicerudna.configuration.AppConstants.Companion.CONTACT_PHONE
import cz.vcelnicerudna.contact.ContactActivity
import cz.vcelnicerudna.main.MainActivity
import cz.vcelnicerudna.photo.PhotoActivity
import cz.vcelnicerudna.prices.PricesActivity
import kotlinx.android.synthetic.main.fragment_app_bar.*

open class BaseActivity : AppCompatActivity() {

    protected lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        action_call.setOnClickListener { handleCallAction() }
        bottom_app_bar.setNavigationOnClickListener { navigateHome() }
        bottom_app_bar.setOnMenuItemClickListener { onNavigationItemSelected(it) }
    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        super.onDestroy()
    }

    private fun navigateHome() {
        val intent = Intent(this, MainActivity::class.java)
        if (!navigatingToSelf(intent)) {
            startActivity(intent)
        }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
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
        if (!navigatingToSelf(intent)) {
            startActivity(intent)
        }
        return true
    }

    private fun navigatingToSelf(intent: Intent): Boolean {
        return intent.component?.shortClassName.equals(this.intent.component?.shortClassName)
    }

    private fun getSnack(view: ConstraintLayout, message: Int, duration: Int): Snackbar {
        val snackBar = Snackbar.make(view, getString(message), duration)
        snackBar.anchorView = action_call
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val snackBarView: View = snackBar.view
        val snackBarTextView: TextView = snackBarView.findViewById(R.id.snackbar_text)
        snackBarTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
        return snackBar
    }

    fun getLongSnack(view: ConstraintLayout, message: Int): Snackbar {
        val snack = getSnack(view, message, LENGTH_LONG)
        snack.setAction(getString(R.string.ok)) { snack.dismiss() }
        return snack
    }

    fun getIndefiniteSnack(view: ConstraintLayout, message: Int, action: Int, onAction: () -> Unit): Snackbar {
        val snack = getSnack(view, message, LENGTH_INDEFINITE)
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

    fun setPromotedFabAction(drawable: Int, action: () -> Unit) {
        val timer = object : CountDownTimer(100, 100) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                action_call.setImageDrawable(ContextCompat.getDrawable(applicationContext, drawable))
                action_call.setOnClickListener { action() }
            }

        }
        timer.start()
    }

    fun handleCallAction() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: %s".format(CONTACT_PHONE))
        startActivity(intent)
    }
}
