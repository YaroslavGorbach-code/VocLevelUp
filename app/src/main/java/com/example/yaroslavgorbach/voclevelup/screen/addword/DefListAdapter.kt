package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord.*
import com.example.yaroslavgorbach.voclevelup.databinding.ItemDefBinding
import com.example.yaroslavgorbach.voclevelup.util.inflateBind

class DefListAdapter(
    private val onSave: (DefItem) -> Unit,
    private val onRemove: (DefItem) -> Unit
) :
    ListAdapter<DefItem, DefListAdapter.DefVh>(
        object : DiffUtil.ItemCallback<DefItem>() {
            override fun areItemsTheSame(oldItem: DefItem, newItem: DefItem) =
                oldItem.def == newItem.def

            override fun areContentsTheSame(oldItem: DefItem, newItem: DefItem) = oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DefVh(parent.inflateBind(ItemDefBinding::inflate))

    override fun onBindViewHolder(holder: DefVh, position: Int) = holder.bind(getItem(position))

    inner class DefVh(private val bind: ItemDefBinding) : RecyclerView.ViewHolder(bind.root) {

        init {
            bind.defBg.setOnClickListener {
                if (!bind.defSaved.isActivated) {
                    onSave(getItem(adapterPosition))
                } else {
                    onRemove(getItem(adapterPosition))
                }
            }
        }

        fun bind(item: DefItem) = with(bind) {
            defText.text = item.def.text
            defTrans.apply {
                text = if (item.def.translations.isNotEmpty()) {
                    item.def.translations.joinToString(separator = "\n") {
                        context.getString(R.string.trans_pattern, it)
                    }
                } else {
                    context.getString(R.string.no_translations_found)
                }
                isVisible = !item.error
                isEnabled = item.def.translations.isNotEmpty()
            }
            defSaved.isActivated = item.saved
        }
    }
}