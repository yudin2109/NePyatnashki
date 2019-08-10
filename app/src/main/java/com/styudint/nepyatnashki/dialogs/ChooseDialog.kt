package com.styudint.nepyatnashki.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.styudint.nepyatnashki.R
import kotlinx.android.synthetic.main.choose_dialog.view.*

class ChooseDialog(private var ctx: Context, private var list: List<String>) : AppCompatDialogFragment() {
    private var okCallback: ((String) -> Unit)? = null
    private var dismissCallback: (() -> Unit)? = null

    private fun createView(): View {
        return LayoutInflater.from(ctx).inflate(R.layout.choose_dialog, null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = createView()
        view.spinner.adapter = ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, list)
        return AlertDialog.Builder(ctx)
            .setView(view)
            .setPositiveButton(R.string.ok_string) { _, _ -> okCallback?.invoke(view.spinner.selectedItem as String) }
            .setNegativeButton(R.string.cancel_string) { _, _ -> dismissCallback?.invoke() }
            .create()
    }

    fun setPositiveCallback(cb: (String) -> Unit) {
        okCallback = cb
    }

    fun setNegativeCallback(cb: () -> Unit) {
        dismissCallback = cb
    }
}