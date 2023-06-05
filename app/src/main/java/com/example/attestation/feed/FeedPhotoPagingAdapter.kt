package com.example.attestation.feed

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.attestation.FeedPhoto
import com.example.attestation.databinding.FeedPhotoSimpleBinding

class FeedPhotoPagingAdapter(
    private val onClick: (FeedPhoto) -> Unit
) :
    PagingDataAdapter<FeedPhoto, MyViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FeedPhotoSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            //Установка данных
            authorName.text = item?.nameAuthor
            authorNickname.text = "@${item?.usernameAuthor}"
            sumLikes.text = item?.likes.toString()
            if (item?.liked_by_user == true) indexLike.setColorFilter(Color.RED)
            authorAvatar.load(item?.userAvatar) {
                transformations(CircleCropTransformation())
            }
            mainPhoto.load(item?.urlsSmall) {
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
class DiffUtilCallback : DiffUtil.ItemCallback<FeedPhoto>() {
    override fun areItemsTheSame(oldItem: FeedPhoto, newItem: FeedPhoto): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: FeedPhoto,
        newItem: FeedPhoto
    ): Boolean =
        oldItem == newItem
}

class MyViewHolder(val binding: FeedPhotoSimpleBinding) : RecyclerView.ViewHolder(binding.root)