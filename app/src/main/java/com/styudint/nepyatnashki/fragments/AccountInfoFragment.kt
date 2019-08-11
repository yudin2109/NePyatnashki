package com.styudint.nepyatnashki.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.styudint.nepyatnashki.NePyatnashkiApp
import com.styudint.nepyatnashki.R
import com.styudint.nepyatnashki.account.AccountManager
import com.styudint.nepyatnashki.account.AccountManagerListener
import kotlinx.android.synthetic.main.account_info_fragment.*
import javax.inject.Inject

class AccountInfoFragment(private val application: NePyatnashkiApp) : Fragment(), AccountManagerListener {
    @Inject
    lateinit var accountManager: AccountManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.account_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        application.appComponent.inject(this)
        accountManager.subscribe(this)
    }

    override fun accountHasChanged() {
        val user = accountManager.currentUser()
        if (user != null) {
            login.text = user.displayName
            email.text = user.email
        } else { // Might be unused, but for debug it can be useful
            login.text = "Unknown"
            email.text = ""
        }
    }
}