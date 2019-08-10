package com.styudint.nepyatnashki

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.styudint.nepyatnashki.dialogs.ChooseDialog
import com.styudint.nepyatnashki.settings.ControlMode
import com.styudint.nepyatnashki.settings.ControlModeInfo
import com.styudint.nepyatnashki.settings.SettingsManager
import com.styudint.nepyatnashki.settings.SettingsManagerListener
import kotlinx.android.synthetic.main.settings_page.*
import javax.inject.Inject

class SettingsPage : FragmentActivity(), SettingsManagerListener {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_page)

        (application as NePyatnashkiApp).appComponent.inject(this)

        settingsManager.subscribe(this)

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

    override fun settingsChanged() {
        controlMode.setText(ControlModeInfo[settingsManager.controlMode()]!!)
    }
}