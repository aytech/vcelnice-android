package cz.vcelnicerudna.contact

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cz.vcelnicerudna.BaseActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.data.RepositoryImpl
import cz.vcelnicerudna.databinding.ActivityContactBinding
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : BaseActivity(), ContactContract.ViewInterface {

    private lateinit var viewModel: ContactViewModel
    private lateinit var presenter: ContactContract.PresenterInterface

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private fun postContactMessage() {
        // TODO: implement loading indicator
        if (viewModel.canPostMessage()) {
            presenter.postContactMessage(viewModel.getEmailMessage())
        } else {
            getLongSnack(main_view_contact, bottom_app_bar_contact, viewModel.validationMessage).show()
        }
    }

    override fun onMessageSent() {
        viewModel.clearForm()
        getLongSnack(main_view_contact, bottom_app_bar_contact, R.string.contact_sent_success).show()
    }

    override fun onMessageSentError() {
        getLongSnack(main_view_contact, bottom_app_bar_contact, R.string.network_error).show()
    }

    override fun onMessageSendComplete() {
        // TODO: cancel loading indicator here
    }
}
