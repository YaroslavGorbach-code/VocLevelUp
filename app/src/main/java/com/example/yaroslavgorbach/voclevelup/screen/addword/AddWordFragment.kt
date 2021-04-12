package com.example.yaroslavgorbach.voclevelup.screen.addword

import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yaroslavgorbach.voclevelup.R
import com.example.yaroslavgorbach.voclevelup.component.AddWord
import com.example.yaroslavgorbach.voclevelup.component.AddWord.Translation
import com.example.yaroslavgorbach.voclevelup.data.Definition
import com.example.yaroslavgorbach.voclevelup.databinding.FragmentAddWordBinding
import com.example.yaroslavgorbach.voclevelup.util.consume
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    interface Host {
        fun onWordAdded(text: String)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm by viewModels<AddWordViewModel>()
        val binding = FragmentAddWordBinding.bind(view)

        with(vm.addWord) {
            binding.addWordSave.setOnClickListener { onSave() }
            binding.addWordInput.editText?.doAfterTextChanged { onWordInput(it.toString()) }
            translation.observe(viewLifecycleOwner) {
                binding.addWordProgress.isVisible = it is Translation.Progress
                binding.addWordTranslation.isVisible = it !is Translation.Progress

                when (it) {
                    Translation.Idle -> binding.addWordTranslation.text = ""
                    Translation.Fail -> { binding.addWordTranslation.setText(R.string.cant_load_translation) }
                    is Translation.Success -> {
                        binding.addWordTranslation.text = it.result.map(Definition::text).toString()
                    }
                }
            }

            saveEnabled.observe(viewLifecycleOwner) {
                binding.addWordSave.isEnabled = it
            }

            binding.addWordInput.editText?.filters =
                arrayOf(InputFilter.LengthFilter(maxWordLength))

            languages.observe(viewLifecycleOwner) { languages ->
                binding.addWordLang.text = languages.first().toString()
                binding.addWordLang.setOnClickListener { v ->
                    val popupMenu = PopupMenu(requireContext(), v, Gravity.TOP)
                    languages.drop(1).forEach { lang ->
                        popupMenu.menu.add(lang.toString()).setOnMenuItemClickListener {
                            chooseLang(lang)
                            true
                        }
                    }
                    popupMenu.show()
                }
            }
            onWordAdded.consume(viewLifecycleOwner, (activity as Host)::onWordAdded)
        }
    }
}