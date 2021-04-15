package com.example.yaroslavgorbach.voclevelup.screen.dict

import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentDictBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.Math.abs
import java.lang.Math.round


class DictView(
    private val binding: FragmentDictBinding,
    private val callback: Callback
) {

    interface Callback {
        fun onAdd()
        fun onSwipe(word: Word)
        fun onClick(word: Word)
    }

    private val listAdapter = WordListAdapter(callback::onClick)

    init {
        binding.dictAdd.setOnClickListener { callback.onAdd() }
        binding.dictList.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addItemDecoration(ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.START or ItemTouchHelper.END
            ) {
                private val swipeBg = ContextCompat.getDrawable(context, R.drawable.word_swipe_bg)!!

                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    callback.onSwipe(listAdapter.currentList[viewHolder.adapterPosition])
                }

                override fun onChildDraw(
                    canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
                ) {
                    val item = viewHolder.itemView
                    val clipLeft = if (dX >= 0) 0 else item.width + dX.toInt()
                    val clipRight = if (dX >= 0) dX.toInt() else item.width
                    canvas.clipRect(clipLeft, item.top, clipRight, item.bottom)
                    swipeBg.setBounds(item.left, item.top, item.right, item.bottom)
                    swipeBg.alpha = round((1 - abs(dX / item.width)) * 255).toInt()
                    swipeBg.draw(canvas)
                    super.onChildDraw(
                        canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                }
            }).also { it.attachToRecyclerView(this) })
        }
    }

    fun setWords(words: List<Word>) = with(binding) {
        listAdapter.submitList(words)
        dictEmpty.isVisible = words.isEmpty()
    }

    fun setLoading(loading: Boolean) = with(binding) {
        dictProgress.isVisible = loading
    }

    fun showRemoveWordUndo(undo: () -> Unit) = with(binding) {
        Snackbar.make(root, R.string.word_removed, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { undo() }
            .show()
    }
}