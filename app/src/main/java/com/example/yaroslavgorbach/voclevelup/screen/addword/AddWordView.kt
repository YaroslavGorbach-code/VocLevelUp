package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.text.InputFilter
import android.view.Gravity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.component.AddWord.Translation.*
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding

class AddWordView(
    private val binding: FragmentAddWordBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onSave()
        fun onInput(input: String)
        fun onLangClick(lang: Language)
    }

    private val adapter = TransListAdapter()


    init {
        with(binding) {
            addWordSave.setOnClickListener { callback.onSave() }
            addWordInput.editText?.doAfterTextChanged { callback.onInput(it.toString()) }
            addWortTransList.adapter = adapter
            addWortTransList.layoutManager = LinearLayoutManager(root.context)
        }
    }

    fun setTranslation(state: AddWord.Translation) = with(binding) {
        println(state)
        addWordProgress.isVisible = state is Progress
        addWordLoadError.isVisible = state is Fail
        addWordEmptyList.isVisible = state is Success && state.result.isEmpty()
        adapter.submitList(if (state is Success) state.result else emptyList())
    }


    fun setSaveEnabled(enabled: Boolean) = with(binding) {
        addWordSave.isEnabled = enabled
    }

    fun setMaxWordLength(length: Int) = with(binding) {
        addWordInput.editText?.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    fun setLanguages(languages: List<Language>) = with(binding) {
        addWordLang.text = languages.first().toString()
        addWordLang.setOnClickListener { v ->
            val popupMenu = PopupMenu(root.context, v, Gravity.TOP)
            languages.drop(1).forEach { lang ->
                popupMenu.menu.add(lang.toString()).setOnMenuItemClickListener {
                    callback.onLangClick(lang)
                    true
                }
            }
            popupMenu.show()
        }
    }
}