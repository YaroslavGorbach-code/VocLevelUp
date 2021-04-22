package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord.*
import com.example.yaroslavgorbach.voclevelup.component.AddWord.DefState.*
import com.example.yaroslavgorbach.voclevelup.data.Language
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.google.android.material.snackbar.Snackbar


class AddWordView(
    private val bind: FragmentAddWordBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onSave(item: DefItem)
        fun onOpen(item: DefItem)
        fun onInput(input: String)
        fun onLangClick(lang: Language)
        fun onUp()
        fun onRetry()
    }

    private val listAdapter = DefListAdapter { item ->
        if (!item.saved || item.trans?.any { !it.second } == true) {
            callback.onSave(item)
        } else {
            callback.onOpen(item)
        }
    }

    private val errorSnack = Snackbar.make(bind.root, R.string.cant_load_translations, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.retry) { callback.onRetry() }
        .also {
            bind.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {}
                override fun onViewDetachedFromWindow(v: View?) {
                    it.dismiss()
                }
            })
        }

    init {
        bind.addWordInput.apply {
            doAfterTextChanged { callback.onInput(it.toString()) }
            val imm = context.getSystemService<InputMethodManager>()
            imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
        bind.addWordTransList.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        bind.addWordToolbar.setNavigationOnClickListener { callback.onUp() }
    }

    fun setDefState(state: DefState) = with(bind) {
        addWordProgress.isVisible = state is DefState.Loading
        listAdapter.apply {
            if (state is Error) {
                errorSnack.show()
            } else {
                errorSnack.dismiss()
            }
            submitList(if (state is Data) state.items else emptyList())
        }
    }

    fun setMaxWordLength(length: Int) = with(bind) {
        addWordInput.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    fun setLanguages(languages: List<Language>) = with(bind) {
        val langItem = addWordToolbar.menu.findItem(R.id.menu_lang)
        langItem.subMenu.clear()
        languages.forEachIndexed { i, lang ->
            val item = langItem.subMenu.add(lang.nativeName).setOnMenuItemClickListener {
                callback.onLangClick(lang)
                true
            }
            if (i == 0) {
                item.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_done)?.apply {
                    setTintList(
                        ContextCompat.getColorStateList(root.context, R.color.target_lang_tint)
                    )
                }
            }
        }
    }
}