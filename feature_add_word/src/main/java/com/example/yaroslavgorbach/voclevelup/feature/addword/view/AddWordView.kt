package com.example.yaroslavgorbach.voclevelup.feature.addword.view

import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.data.api.Language
import com.example.yaroslavgorbach.voclevelup.feature.addword.R
import com.example.yaroslavgorbach.voclevelup.feature.addword.model.AddWord.*
import com.example.yaroslavgorbach.voclevelup.feature.addword.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.feature.addword.model.AddWord
import com.example.yaroslavgorbach.voclevelup.feature.setNavAsBack
import com.example.yaroslavgorbach.voclevelup.feature.softInput
import com.google.android.material.snackbar.Snackbar


internal class AddWordView(
    private val bind: FragmentAddWordBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onOpen(item: DefItem)
        fun onSave(item: DefItem)
        fun onInput(input: String)
        fun onLangClick(lang: Language)
        fun onRetry()
        fun onInputDone(text: String)
        fun onCompClick(text: String)
    }

    private val defAdapter = DefListAdapter { item ->
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

    private var handlingCompletion = false
    private val compAdapter = CompletionAdapter {
        handlingCompletion = true
        bind.addWordInput.text.apply {
            replace(0, length, it)
            callback.onCompClick(this.toString()) // get new input with applied filters
        }
        handlingCompletion = false
    }

    init {
        bind.addWordInput.apply {
            doAfterTextChanged {
                if (!handlingCompletion) {
                    callback.onInput(it.toString())
                }
            }
            softInput.show(this)
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callback.onInputDone(v.text.toString())
                    true
                } else {
                    false
                }
            }
        }
        bind.addWordDefList.apply {
            adapter = defAdapter
            layoutManager = LinearLayoutManager(context)
        }
        bind.addWordToolbar.setNavAsBack()
        bind.addWordCompList.apply {
            adapter = compAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
    }

    fun setState(state: State) {
        bind.addWordProgress.isVisible = state is State.Loading
        bind.addWordDefList.apply {
            if ((state as? State.Definitions)?.error == true) {
                errorSnack.show()
            } else {
                errorSnack.dismiss()
            }
            defAdapter.submitList((state as? State.Definitions)?.items ?: emptyList())
            isInvisible = state !is State.Definitions // gone delays animations -> see blink on new list shown
        }
        bind.addWordCompList.apply {
            compAdapter.submitList((state as? State.Completions)?.items ?: emptyList())
            isVisible = state is State.Completions
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
                item.icon = root.context.getDrawable(R.drawable.ic_done)?.apply {
                    setTintList(
                        ContextCompat.getColorStateList(root.context, R.color.target_lang_tint)
                    )
                }
            }
        }
    }
}