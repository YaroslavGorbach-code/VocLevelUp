package com.example.yaroslavgorbach.voclevelup.feature.dictionary.view

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.data.api.Word
import com.example.yaroslavgorbach.voclevelup.feature.SwipeDismissDecor
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.R
import com.example.yaroslavgorbach.voclevelup.feature.dictionary.databinding.FragmentDictBinding
import com.google.android.material.snackbar.Snackbar

internal class DictView(
    private val bind: FragmentDictBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onAdd(srcView: View)
        fun onSwipe(word: Word)
        fun onClick(word: Word, srcView: View)
    }

    private val listAdapter = WordListAdapter(callback::onClick)

    init {
        bind.dictAdd.setOnClickListener { callback.onAdd(it) }
        bind.dictList.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            val swipeDecor =
                SwipeDismissDecor(context.getDrawable(R.drawable.delete_item_hint_bg)!!) {
                    callback.onSwipe(listAdapter.currentList[it.adapterPosition])
                }
            addItemDecoration(swipeDecor.also { it.attachToRecyclerView(this) })
        }
    }

    fun setWords(words: List<Word>) = with(bind) {
        listAdapter.submitList(words)
        dictEmpty.isVisible = words.isEmpty()
    }

    fun setLoading(loading: Boolean) = with(bind) {
        dictProgress.isVisible = loading
    }

    fun showRemoveWordUndo(undo: () -> Unit) = with(bind) {
        Snackbar.make(root, R.string.word_removed, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { undo() }
            .show()
    }
}