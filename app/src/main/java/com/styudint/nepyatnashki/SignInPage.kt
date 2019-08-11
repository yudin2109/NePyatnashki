package com.styudint.nepyatnashki

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.styudint.nepyatnashki.account.AccountManager
import com.styudint.nepyatnashki.account.AuthenticationError
import com.styudint.nepyatnashki.account.PendingAuthentication
import com.styudint.nepyatnashki.account.SuccessfullyAuthenticated
import kotlinx.android.synthetic.main.sign_in_page.*
import javax.inject.Inject

class SignInPage : AppCompatActivity() {
    @Inject
    lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_page)

        (application as NePyatnashkiApp).appComponent.inject(this)

        signInButton.setOnClickListener {
            accountManager.signInWithEmail(email.text.toString(), password.text.toString())
                .observe(this, Observer {
                    when (it) {
                        PendingAuthentication -> showLoading()
                        SuccessfullyAuthenticated -> showSuccessState()
                        is AuthenticationError -> showError(it.error)
                    }
                })
        }

        registerButton.setOnClickListener {
            accountManager.register(email.text.toString(), password.text.toString())
                .observe(this, Observer {
                    when (it) {
                        PendingAuthentication -> showLoading()
                        SuccessfullyAuthenticated -> showSuccessState()
                        is AuthenticationError -> showError(it.error)
                    }
                })
        }
    }

    private fun showLoading() {
        info.text = "Loading please wait"
        info.setBackgroundColor(resources.getColor(R.color.grey))
    }

    private fun showSuccessState() {
        info.text = "Successfully authenticated"
        info.setBackgroundColor(resources.getColor(R.color.success_translucent_color))
    }

    private fun showError(error: String) {
        info.text = "Error occurred while authentication\n$error"
        info.setBackgroundColor(resources.getColor(R.color.error_translucent_color))
    }
}