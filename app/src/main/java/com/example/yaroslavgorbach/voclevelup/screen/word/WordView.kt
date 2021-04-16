package com.example.yaroslavgorbach.voclevelup.screen.word

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentWordBinding

class WordView(
    private val binding: FragmentWordBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onUp()
        fun onDelete()
        fun onReorderTrans(newTrans: List<String>)
        fun onAddTrans()
        fun onEditTrans(trans: String)
    }

    private val transAdapter = TransListAdapter(callback::onReorderTrans, callback::onEditTrans)

    init {
        with(binding) {
            wordToolbar.apply {
                setNavigationOnClickListener { callback.onUp() }
                menu.findItem(R.id.menu_word_del).setOnMenuItemClickListener {
                    callback.onDelete()
                    true
                }
            }
            wordTransList.apply {
                adapter = transAdapter
                layoutManager = LinearLayoutManager(context)
            }
            wordAddTrans.setOnClickListener { callback.onAddTrans() }
        }
    }

    fun setTranslations(trans: List<String>?) = with(binding) {
        wordTransProgress.isVisible = trans == null
        if (trans != null) {
            transAdapter.submitList(trans)
        }
    }

    fun setWordText(text: String) = with(binding) {
        wordText.text = text
    }

}