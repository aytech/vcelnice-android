package cz.vcelnicerudna.contact

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.databinding.ActivityContactBinding
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contact.*
import java.net.URLEncoder

class ContactActivity : BaseActivity(), ContactContract.ViewInterface {

    private lateinit var viewModel: ContactViewModel

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityContactBinding>(this, R.layout.activity_contact)
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        binding.viewModel = viewModel

        send_message.setOnClickListener { postContactMessage() }
    }

    private fun postContactMessage() {
        if (viewModel.canPostMessage()) {

        } else {
            getThemedSnackBar(main_view, viewModel.validationMessage, LENGTH_LONG).show()
        }
    }

    //    private fun postContactMessage() {
//        val emailParam: String = email.text.toString()
//        val messageParam: String = URLEncoder.encode(message.text.toString(), StringConstants.UTF_8)
//        val compositeDisposable = CompositeDisposable()
//        val disposable: Disposable = vcelniceAPI.postContactMessage(emailParam, messageParam)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        {
//                            email.text?.clear()
//                            message.text?.clear()
//                            getThemedSnackBar(main_view, R.string.contact_sent_success, Snackbar.LENGTH_LONG)
//                                    .show()
//                            compositeDisposable.dispose()
//                        },
//                        {
//                            getThemedSnackBar(main_view, R.string.network_error, Snackbar.LENGTH_LONG)
//                                    .show()
//                            compositeDisposable.dispose()
//                        }
//                )
//        compositeDisposable.add(disposable)
//    }
}
