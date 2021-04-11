package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.trimmedLength
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.util.input
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature.State.*
import com.example.yaroslavgorbach.voclevelup.feature.TranslationFeature
import com.example.yaroslavgorbach.voclevelup.repo
import java.util.*

class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    interface Host {
        fun onWordAdded(text: String)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val vm by viewModels<AddWordViewModel>()
        val v = AddWordImp(FragmentAddWordBinding.bind(view),
            object: AddWordView.Callback{
            override fun onWordInput(text: String) = vm.onWordInput(text)
            override fun onSaveClick() = vm.onSave()
        })
        vm.translation.observe(viewLifecycleOwner, v::setTranslation)
        vm.saveEnabled.observe(viewLifecycleOwner, v::setSaveEnable)

        lifecycleScope.launchWhenStarted {
            for (event in vm.onWordAdded) {
                (activity as Host).onWordAdded(event)
            }
        }
    }
}