package com.styudint.nepyatnashki

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.styudint.nepyatnashki.account.AccountManager
import com.styudint.nepyatnashki.account.AccountManagerListener
import com.styudint.nepyatnashki.dialogs.ChooseDialog
import com.styudint.nepyatnashki.fragments.AccountInfoFragment
import com.styudint.nepyatnashki.settings.ControlMode
import com.styudint.nepyatnashki.settings.ControlModeInfo
import com.styudint.nepyatnashki.settings.SettingsManager
import com.styudint.nepyatnashki.settings.SettingsManagerListener
import kotlinx.android.synthetic.main.settings_page.*
import kotlinx.android.synthetic.main.settings_page_sign_in_layout.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsPage : FragmentActivity(), SettingsManagerListener, AccountManagerListener {
    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_page)

        (application as NePyatnashkiApp).appComponent.inject(this)

        settingsManager.subscribe(this)
        accountManager.subscribe(this)

        controlModeSetting.setOnClickListener {
            val dialog = ChooseDialog(this, prepareStrings(ControlModeInfo))
            dialog.setPositiveCallback {
                settingsManager.changeControlMode(backDecode(it))
            }
            dialog.show(supportFragmentManager, "choose_control_type")
        }
    }

    private fun prepareStrings(map: Map<ControlMode, Int>): List<String> {
        val result = ArrayList<String>()
        map.forEach {
            result.add(resources.getString(it.value))
        }
        return result
    }

    private fun backDecode(str: String): ControlMode {
        ControlModeInfo.forEach {
            if (str == resources.getString(it.value)) {
                return it.key
            }
        }
        throw IllegalStateException("Something went wrong, cannot decode string: $str")
    }

    // SettingsManagerListener
    override fun settingsChanged() {
        MainScope().launch {
            controlMode.setText(ControlModeInfo[settingsManager.controlMode()]!!)
        }
    }

    // AccountManagerListener
    override fun accountHasChanged() {
        MainScope().launch {
            if (accountManager.currentUser() == null) {
                setupLoginButton()
            } else {
                setupAccountInfo()
            }
        }
    }

    private fun setupLoginButton() {
        accountAnchor.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.settings_page_sign_in_layout, accountAnchor)
        view.signInButton.setOnClickListener {
            openSignInPage()
        }
    }

    private fun setupAccountInfo() {
        accountAnchor.removeAllViews()
        val fragment = AccountInfoFragment(application as NePyatnashkiApp)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.accountAnchor, fragment)
        transaction.commit()
    }

    override fun onPause() {
        super.onPause()
        accountManager.unsubscribe(this)
    }

    override fun onResume() {
        super.onResume()
        accountManager.subscribe(this)
    }

    private fun openSignInPage() {
        startActivity(Intent(this, SignInPage::class.java))
    }
}