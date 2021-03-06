package com.example.yaroslavgorbach.voclevelup.feature.worddetails.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.R
import com.example.yaroslavgorbach.voclevelup.feature.worddetails.databinding.DialogTransBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal class EditTransDialog : DialogFragment() {

    interface Host {
        fun onEditTrans(trans: String, newText: String)
        fun onDeleteTrans(trans: String)
    }

    companion object {
        fun argsOf(trans: String) = bundleOf("trans" to trans)
        private val EditTransDialog.trans get() = requireArguments()["trans"] as String
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bind = DialogTransBinding.inflate(LayoutInflater.from(context))
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(bind.root)
            .setTitle(R.string.edit_translation)
            .setPositiveButton(R.string.save) { _, _ ->
                val input = bind.transInput.text.toString()
                if (input != trans) {
                    (parentFragment as Host).onEditTrans(trans, input)
                }
            }
            .setNeutralButton(R.string.delete) { _, _ ->
                (parentFragment as Host).onDeleteTrans(trans)
            }
            .create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setOnShowListener {
            val saveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            bind.transInput.apply {
                doAfterTextChanged { saveBtn.isEnabled = !it.isNullOrBlank() }
                setText(trans)
                selectAll()
            }
        }
        return dialog
    }
}