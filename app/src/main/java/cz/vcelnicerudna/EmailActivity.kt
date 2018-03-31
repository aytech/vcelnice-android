package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_email.*

class EmailActivity : BaseActivity() {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        super.actionBarToggleWithNavigation(this)

        send_message.setOnClickListener { _ ->
            if (emailValid() && messageValid()) {
                email_error.visibility = View.INVISIBLE
                postContactMessage()
            }
        }
    }

    private fun emailValid(): Boolean {
        if (TextUtils.isEmpty(email.text)
                || !Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            email_error.text = getString(R.string.enter_valid_email)
            email_error.visibility = View.VISIBLE
            return false
        }
        email_error.visibility = View.INVISIBLE
        return true
    }

    private fun messageValid(): Boolean {
        if (TextUtils.isEmpty(message.text)) {
            message_error.text = getString(R.string.enter_message)
            message.visibility = View.VISIBLE
            return false
        }
        message_error.visibility = View.INVISIBLE
        return true
    }

    private fun postContactMessage() {
        vcelniceAPI.postContactMessage(email.text.toString(), message.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            email.text.clear()
                            message.text.clear()
                            getThemedSnackbar(main_view, R.string.contact_sent_success, Snackbar.LENGTH_LONG)
                                    .show()
                        },
                        {
                            getThemedSnackbar(main_view, R.string.network_error, Snackbar.LENGTH_LONG)
                                    .show()
                        }
                )
    }
}
