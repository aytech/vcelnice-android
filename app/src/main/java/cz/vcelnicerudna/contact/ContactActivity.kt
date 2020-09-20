package cz.vcelnicerudna.contact

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.databinding.ActivityContactBinding
import cz.vcelnicerudna.interfaces.VcelniceAPI
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : BaseActivity(), ContactContract.ViewInterface {

    private lateinit var viewModel: ContactViewModel
    private lateinit var presenter: ContactContract.PresenterInterface

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityContactBinding>(this, R.layout.activity_contact)
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        binding.viewModel = viewModel

        presenter = ContactPresenter(this, RepositoryImpl())

        send_message.setOnClickListener { postContactMessage() }
        action_call.setOnClickListener { handleCallAction() }
        bottom_app_bar_contact.setNavigationOnClickListener { navigateHome() }
        bottom_app_bar_contact.setOnMenuItemClickListener { onNavigationItemSelected(it, R.id.contact_page) }
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
