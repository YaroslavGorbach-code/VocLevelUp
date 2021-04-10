package com.example.yaroslavgorbach.voclevelup.util

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun EditText.input(scope: CoroutineScope = GlobalScope): ReceiveChannel<String> {
    val channel = Channel<String>(Channel.UNLIMITED)
    scope.launch {
        channel.send(text.toString())
    }
    doAfterTextChanged {
        scope.launch {
            channel.send(it.toString())
        }
    }
    return channel
}