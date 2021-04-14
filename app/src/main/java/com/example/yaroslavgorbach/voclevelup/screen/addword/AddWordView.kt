package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord.*
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding

class AddWordView(
    private val binding: FragmentAddWordBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onSave(item: DefItem)
        fun onRemove(item: DefItem)
        fun onInput(input: String)
        fun onLangClick(lang: Language)
        fun onUp()
    }

    private val adapter = DefListAdapter(callback::onSave, callback::onRemove)

    init {
        with(binding) {
            addWordInput.doAfterTextChanged { callback.onInput(it.toString()) }
            addWortTransList.adapter = adapter
            addWortTransList.layoutManager = LinearLayoutManager(root.context)
            addWordToolbar.setNavigationOnClickListener { callback.onUp() }
            val imm = root.context.getSystemService<InputMethodManager>()
            imm?.showSoftInput(addWordInput, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun setDefState(state: DefState) = with(binding) {
        addWordProgress.isVisible = state is DefState.Loading
        addWordLoadError.isVisible = state is Error
        addWordEmptyList.isVisible = state is DefState.Data && state.items.isEmpty()
        adapter.submitList(if (state is DefState.Data) state.items else emptyList())
    }

    fun setMaxWordLength(length: Int) = with(binding) {
        addWordInput.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    fun setLanguages(languages: List<Language>) = with(binding) {
        val langItem = addWordToolbar.menu.findItem(R.id.menu_lang)
        langItem.subMenu.clear()
        languages.forEachIndexed { i, lang ->
            val item = langItem.subMenu.add(lang.nativeName).setOnMenuItemClickListener {
                callback.onLangClick(lang)
                true
            }
            if (i == 0) {
                item.icon = root.context.getDrawable(R.drawable.ic_done)?.apply {
                    setTintList(ContextCompat.getColorStateList(root.context,
                        R.color.target_lang_tint))
                }
            }
        }
    }
}