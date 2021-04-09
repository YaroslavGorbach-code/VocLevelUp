package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.repo
import kotlinx.coroutines.launch

class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    interface Host{
        fun closeAddWord()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init save button
        val inputEt = view.findViewById<EditText>(R.id.addWordInput)
        val saveBtn = view.findViewById<View>(R.id.addWordSave)
        inputEt.doAfterTextChanged {
            saveBtn.isEnabled = it?.isNotBlank() == true
        }

        saveBtn.setOnClickListener {
            saveBtn.isEnabled = false
            lifecycleScope.launch {
                repo.addWord(inputEt.text.toString())
                (activity as Host).closeAddWord()
            }
        }
    }
}