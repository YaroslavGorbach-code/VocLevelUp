package com.example.yaroslavgorbach.voclevelup.feature.worddetails.model
import androidx.lifecycle.LiveData
import com.example.yaroslavgorbach.voclevelup.util.LiveEvent

internal interface WordDetails {
    val text: LiveData<String>
    val pron: LiveData<String>
    val translations: LiveData<List<String>?>
    val onTransDeleted: LiveEvent<suspend () -> Unit>
    val onWordDeleted: LiveEvent<suspend () -> Unit>
    fun onReorderTrans(newTrans: List<String>)
    fun onAddTrans(text: String)
    fun onEditTrans(trans: String, newText: String)
    fun onDeleteTrans(trans: String)
    fun onDeleteWord()
}