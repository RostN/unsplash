package com.example.attestation.account

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.attestation.LoadPhotoResponse
import com.example.attestation.databinding.FeedPhotoSimpleBinding

class AccountAdapter(
    private val onClick: (LoadPhotoResponse) -> Unit
) :
    PagingDataAdapter<LoadPhotoResponse, MyViewHolderAccount>(DiffUtilCallbackAccount()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderAccount {
        return MyViewHolderAccount(
            FeedPhotoSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolderAccount, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            //Установка данных
            authorName.text = ""
            authorNickname.text = ""
            sumLikes.text = item?.likes.toString()
            if (item?.liked_by_user == true) indexLike.setColorFilter(Color.RED)
            authorAvatar.alpha = 0f
            mainPhoto.load(item?.urls?.regular) {
                target {
                    progress.isVisible = false
                    interfacePhoto.isVisible = true
                    mainPhoto.setImageDrawable(it)
                }
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let { onClick(item) }
        }
    }
}

//Дифутил
class DiffUtilCallbackAccount : DiffUtil.ItemCallback<LoadPhotoResponse>() {
    override fun areItemsTheSame(oldItem: LoadPhotoResponse, newItem: LoadPhotoResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: LoadPhotoResponse,
        newItem: LoadPhotoResponse
    ): Boolean =
        oldItem == newItem
}

class MyViewHolderAccount(val binding: FeedPhotoSimpleBinding) :
    RecyclerView.ViewHolder(binding.root)