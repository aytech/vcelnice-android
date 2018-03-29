package cz.vcelnicerudna

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import kotlinx.android.synthetic.main.content_email.*

class EmailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        super.actionBarToggleWithNavigation(this)

        send_message.setOnClickListener{view ->
            if (TextUtils.isEmpty(email.text)) {
                email_error_message.text = getString(R.string.email_empty)
                email_error_message.visibility = View.VISIBLE
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                email_error_message.text = getString(R.string.email_invalid)
                email_error_message.visibility = View.VISIBLE
            } else {
                email_error_message.visibility = View.INVISIBLE
                Snackbar.make(view, "Send message: ${message.text}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
