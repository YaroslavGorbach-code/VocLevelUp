package com.example.yaroslavgorbach.voclevelup.feature.addword.view

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.feature.addword.model.AddWord.*
import com.example.yaroslavgorbach.voclevelup.feature.addword.R
import com.example.yaroslavgorbach.voclevelup.feature.addword.databinding.ItemDefBinding
import com.example.yaroslavgorbach.voclevelup.feature.addword.model.AddWord
import com.example.yaroslavgorbach.voclevelup.feature.inflateBind

internal class DefListAdapter(
    private val onClick: (DefItem) -> Unit
) :
    ListAdapter<DefItem, DefListAdapter.DefVh>(
        object : DiffUtil.ItemCallback<DefItem>() {
            override fun areItemsTheSame(oldItem: DefItem, newItem: DefItem) =
                oldItem.text == newItem.text

            override fun areContentsTheSame(oldItem: DefItem, newItem: DefItem) = oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DefVh(parent.inflateBind(ItemDefBinding::inflate))

    override fun onBindViewHolder(holder: DefVh, position: Int) = holder.bind(getItem(position))

    inner class DefVh(private val bind: ItemDefBinding) : RecyclerView.ViewHolder(bind.root) {

        init {
            bind.defBg.setOnClickListener { onClick(getItem(adapterPosition)) }
        }

        fun bind(item: DefItem) {
            bind.defText.text = item.text
            bind.defTrans.apply {
                val trans = item.trans
                when {
                    trans == null -> isVisible = false
                    trans.isEmpty() -> {
                        isVisible = true
                        isEnabled = false
                        setText(R.string.no_translations_found)
                    }
                    else -> {
                        isVisible = true
                        isEnabled = true
                        text = trans.joinToString(separator = "\n") {
                            val pattern =
                                if (it.second) R.string.trans_pattern else R.string.new_trans_pattern
                            context.getString(pattern, it.first)
                        }
                    }
                }
                bind.defSaved.apply {
                    isVisible = item.saved
                    isActivated = item.trans?.all { it.second } == true
                }
            }
        }
    }
}