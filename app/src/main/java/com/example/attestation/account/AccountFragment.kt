package com.example.attestation.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.attestation.*
import com.example.attestation.auth.*
import com.example.attestation.databinding.FragmentAccountBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    val bundle = Bundle()
    private val accountPhotoAdapter =
        AccountAdapter { LoadPhotoResponse -> onItemClick(LoadPhotoResponse) }
    private val viewModel: AccountViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AccountViewModel() as T
            }
        }
    }
    private val viewModelExit: AuthViewModel by viewModels()
    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModelExit.webLogoutComplete()
        } else {
            // логаут отменен
            // делаем complete тк github не редиректит после логаута и пользователь закрывает CCT
            viewModelExit.webLogoutComplete()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recyclerProfile.adapter = accountPhotoAdapter
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var repo: UserInfoResponse
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                repo = Repository().getMyProfile()
                //Текст геолокации, если есть
                binding.locationText.text = repo.location
                if (repo.location != null) {
                    //Установка геометки
                    val map = "geo:0,0?q=${repo.location}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                    intent.setPackage("com.google.android.apps.maps")
                    //Подчеркивание текста с названием местоположения
                    binding.locationText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    //По нажатию перейти в карты с установленной меткой
                    binding.locationText.setOnClickListener {
                        startActivity(intent)
                    }
                } else binding.location.isVisible = false

                //Установка текстовых значений
                binding.profileBio.text = repo.bio ?: ""
                binding.profileTotalDownloads.text = repo.downloads.toString()
                binding.totalLikes.text = repo.total_likes.toString()
                binding.totalCollections.text = repo.total_collections.toString()
                binding.mailText.text = repo.email
                binding.profileFullName.text = repo.name
                binding.profileNickname.text = "@${repo.username}"
                //Числовые значения
                binding.profileFollowers.text = resources.getQuantityString(
                    R.plurals.profile_followers,
                    repo.followers_count,
                    repo.followers_count
                )
                repo.following_count
                binding.profileFollowing.text = resources.getQuantityString(
                    R.plurals.profile_following,
                    repo.following_count,
                    repo.following_count
                )
                binding.profileTotalPhotos.text = resources.getQuantityString(
                    R.plurals.collection_photo_sum,
                    repo.total_photos,
                    repo.total_photos
                )

                //Аватарка пользователя
                binding.profileAvatar.load(repo.profile_image.medium) {
                    target {
                        binding.progress.isVisible = false
                        binding.accountInterface.isVisible = true
                        binding.profileAvatar.setImageDrawable(it)
                    }
                    transformations(CircleCropTransformation())
                }
                //Загрузка фото самого пользователя
                viewModel.id = repo.username
                viewModel.pagedPhoto.onEach {
                    accountPhotoAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            } catch (e: Exception) {
                println("Error:$e")
            }
        }
        //Кнопка выхода
        binding.exitBtn.setOnClickListener {
            binding.exitWindow.isVisible = true
        }
        //Кнопка отказа
        binding.exitNo.setOnClickListener {
            binding.exitWindow.isVisible = false
        }
        //Кнопка согласия
        binding.exitYes.setOnClickListener {
            viewModelExit.logout()
        }

        viewModelExit.logoutPageFlow.launchAndCollectIn(viewLifecycleOwner) {
            logoutResponse.launch(it)
        }

        viewModelExit.logoutCompletedFlow.launchAndCollectIn(viewLifecycleOwner) {
            var intent = Intent(activity, LoginActivity::class.java)
            activity?.finish()
            editor = sharedPreferences.edit()
            editor.putString("TOKEN", "")
            editor.apply()
            startActivity(intent)
        }
    }

    //Нажатие на элемент
    private fun onItemClick(item: LoadPhotoResponse) {
        bundle.putString("PhotoId", item.id)
        findNavController().navigate(R.id.feedPhotoDetails, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}