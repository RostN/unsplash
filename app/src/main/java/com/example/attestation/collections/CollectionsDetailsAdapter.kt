package com.example.attestation.collections

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.attestation.LoadPhotoResponse
import com.example.attestation.databinding.FeedPhotoSimpleBinding

class CollectionsDetailsAdapter(
    private val onClick: (LoadPhotoResponse) -> Unit
) : PagingDataAdapter<LoadPhotoResponse, MyViewHolderSecond>(DiffUtilCallbackPhoto()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSecond {
        return MyViewHolderSecond(
            FeedPhotoSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolderSecond, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            //Установка данных
            authorName.text = item?.user?.name
            authorNickname.text = item?.user?.username
            sumLikes.text = item?.likes.toString()
            if (item?.liked_by_user == true) indexLike.setColorFilter(Color.RED)
            authorAvatar.load(item?.user?.profile_image?.small) {
                transformations(CircleCropTransformation())
            }
            mainPhoto.load(item?.urls?.small) {
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
class DiffUtilCallbackPhoto : DiffUtil.ItemCallback<LoadPhotoResponse>() {
    override fun areItemsTheSame(oldItem: LoadPhotoResponse, newItem: LoadPhotoResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: LoadPhotoResponse,
        newItem: LoadPhotoResponse
    ): Boolean =
        oldItem == newItem
}

class MyViewHolderSecond(val binding: FeedPhotoSimpleBinding) :
    RecyclerView.ViewHolder(binding.root)