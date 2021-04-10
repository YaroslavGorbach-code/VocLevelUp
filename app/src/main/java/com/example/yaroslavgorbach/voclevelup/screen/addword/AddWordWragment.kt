package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.repo
import com.example.yaroslavgorbach.voclevelup.util.input
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    interface Host{
        fun closeAddWord()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val inputEt = view.findViewById<EditText>(R.id.addWordInput)

        // init save button
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


        // init translation
        val transTv = view.findViewById<TextView>(R.id.addWordTranslation)
        lifecycleScope.launchWhenStarted {
            inputEt.input(lifecycleScope).consumeAsFlow()
                .map { it.trim() }
                .distinctUntilChanged()
                .debounce(500)
                .mapLatest {
                    if (it.isNotBlank()) {
                        try {
                            transTv.isEnabled = false
                            transTv.setText(R.string.loading_translation)
                            repo.getTranslation(it)
                        } catch (e: IOException) {
                            null
                        }
                    } else {
                        ""
                    }
                }
                .collect {
                    if (it != null) {
                        transTv.isEnabled = true
                        transTv.text = it
                    } else {
                        transTv.setText(R.string.cant_load_translation)
                    }
                }
        }
    }

}