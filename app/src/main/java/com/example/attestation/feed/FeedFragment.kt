package com.example.attestation.feed

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.attestation.*
import com.example.attestation.databinding.FragmentFeedBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("StaticFieldLeak")
lateinit var feedContext: Context
var searchRequest = ""

class FeedFragment : Fragment() {

    val bundle = Bundle()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val feedPhotoAdapter =
        FeedPhotoPagingAdapter { FeedPhoto -> onItemClick(FeedPhoto) }
    private val viewModel: FeedViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val feedPhotoDao = (activity?.application as App).db
                return FeedViewModel(feedPhotoDao) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editor = sharedPreferences.edit()
        //Установка адаптера
        binding.feedRecycler.adapter = feedPhotoAdapter
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedContext = requireContext()

        //Проверяем, если был редирект, берём ID фото, чистимм редирект, и открываем фото
        val uri = sharedPreferences.getString("PhotoId-Redirect", "")
        if (uri != "") {
            editor.putString("PhotoId-Redirect", "")
            editor.apply()
            bundle.putString("PhotoId", uri)
            findNavController().navigate(R.id.feedPhotoDetails, bundle)
        }

        //Кнопка поиска отображает или скрывает поисковую строку
        binding.searchBtn.setOnClickListener {
            binding.inputText.isVisible = !binding.inputText.isVisible
            if (binding.inputText.isVisible) binding.inputText.text.clear()
        }

        //Строка ввода
        binding.inputText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                searchRequest = "${binding.inputText.text}"
                startLoad()
                v.hideKeyboard()
                return@OnKeyListener true
            } else false
        })

        //Очистка строки по 2му клику
        binding.inputText.setOnClickListener {
            binding.inputText.text.clear()
        }

        //Запуск ленты
        try {
            startLoad()
        } catch (e: Exception) {
            println("Error 4:$e")
        }
    }

    //Скрытие клавиатуры
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    //Запуск загрузки ленты
    private fun startLoad() {
        try {
            viewModel.pagedPhoto.onEach {
                feedPhotoAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        } catch (e: Exception) {
            println("Error 1: $e")
        }
        try {
            //Обновляем ленту (необходимо для поиска, чтобы результат отображался сразу, без добавления)
            if (searchRequest != "") {
                feedPhotoAdapter.refresh()
            }
        } catch (e: Exception) {
            println("Error 2: $e")
        }
    }

    //Функция нажатия на элемент
    private fun onItemClick(item: FeedPhoto) {
        bundle.putString("PhotoId", item.id)
        findNavController().navigate(R.id.feedPhotoDetails, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//Кастомизированный тост
fun customToast(message: String) {
    val toast = Toast.makeText(feedContext, message, Toast.LENGTH_LONG)
    val toastContainer = toast.view as LinearLayout
    val iconWarning = ImageView(feedContext)
    iconWarning.setImageResource(R.drawable.icon_warning)
    toastContainer.addView(iconWarning, 0)
    toast.show()
}