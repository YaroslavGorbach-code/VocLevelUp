package com.example.yaroslavgorbach.voclevelup.screen.word

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.databinding.ItemTransBinding
import com.example.yaroslavgorbach.voclevelup.util.inflateBinding
import java.util.*

class TransListAdapter(
    private val onReorder: (List<String>) -> Unit
) : ListAdapter<String, TransListAdapter.TransVh>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = true
    }
) {

    private val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {

        private var prevList: List<String>? = null

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
        ): Boolean {
            currentList.toMutableList().apply {
                Collections.swap(this, viewHolder.adapterPosition, target.adapterPosition)
                submitList(this)
            }
            return true
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            prevList?.let {
                if (prevList != currentList) {
                    onReorder(currentList)
                }
                prevList = null
            }
            super.clearView(recyclerView, viewHolder)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                prevList = currentList
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // TODO: 8/16/2020
        }
    })

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(touchHelper)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransVh(parent.inflateBinding(ItemTransBinding::inflate))

    override fun onBindViewHolder(holder: TransVh, position: Int) = holder.bind(getItem(position))

    @SuppressLint("ClickableViewAccessibility")
    inner class TransVh(private val binding: ItemTransBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.transDrag.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(this)
                }
                false
            }
        }

        fun bind(trans: String) = with(binding) {
            transText.text = trans
        }
    }
}