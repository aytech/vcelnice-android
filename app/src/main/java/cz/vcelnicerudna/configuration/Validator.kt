package cz.vcelnicerudna.configuration

import android.text.TextUtils
import android.util.Patterns

class Validator {
    companion object {
        fun isEmail(email: String?): Boolean {
            return if (email.isNullOrEmpty()) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(email as CharSequence).matches()
            }
        }
    }
}