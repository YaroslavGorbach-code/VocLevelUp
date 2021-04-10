package com.example.yaroslavgorbach.voclevelup.screen.word

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.data.Word
import com.example.yaroslavgorbach.voclevelup.data.repo
import com.example.yaroslavgorbach.voclevelup.util.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature.State.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
class WordFragment : Fragment(R.layout.fragment_word) {

    companion object Args {
        fun argsOf(word: Word) = bundleOf("word" to word.text)
        private val WordFragment.wordText get() = requireArguments()["word"] as String
    }


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init word text
        view.findViewById<TextView>(R.id.wordText).text = wordText

        // init translation
        val transTv = view.findViewById<TextView>(R.id.wordTranslation)
        val trans = TranslationFeature(repo)
        val transFlow = transTv.clicks(lifecycleScope).consumeAsFlow()
            .onStart { emit(Unit) } // immediately load translation
            .flatMapLatest { trans.getTranslation(wordText) }

        lifecycleScope.launchWhenStarted {
            transFlow.collect {
                when(it){
                    Fail -> transTv.setText(R.string.reload_translation)
                    Progress -> transTv.setText(R.string.loading_translation)
                    is Success -> transTv.text = it.result
                }
            }
        }
    }
}