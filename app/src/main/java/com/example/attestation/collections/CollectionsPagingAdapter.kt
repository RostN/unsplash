package com.example.attestation.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.attestation.LoadCollections
import com.example.attestation.R
import com.example.attestation.databinding.CollectionPhotoSimpleBinding
import com.example.attestation.feed.feedContext

class CollectionsPagingAdapter(
    private val onClick: (LoadCollections) -> Unit
) :
    PagingDataAdapter<LoadCollections, MyViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CollectionPhotoSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            //Настройка видимости требования подписки
            val sub = item?.cover_photo?.urls?.regular?.contains("plus.unsplash")
            //Настройка текстового окончания
            val sumPhotos = feedContext.resources.getQuantityString(
                R.plurals.collection_photo_sum, item?.total_photos!!, item?.total_photos!!
            )
            //Установка всех значений в форму
            mainPhoto.load(item?.cover_photo?.urls?.regular) {
                target {
                    progress.isVisible = false
                    interfaceCollection.isVisible = true
                    sumOfPhoto.text = sumPhotos
                    mainPhoto.setImageDrawable(it)
                }
            }
            authorAvatar.load(item?.user?.profile_image?.small) {
                transformations(CircleCropTransformation())
            }
            unsplashPlus.isVisible = sub!!
            collectionName.text = item?.title
            authorName.text = item?.user?.name
            authorNickname.text = item?.user?.username
        }
        holder.binding.root.setOnClickListener {
            item?.let { onClick(item) }
        }
    }
}

//Дифутил
class DiffUtilCallback : DiffUtil.ItemCallback<LoadCollections>() {
    override fun areItemsTheSame(oldItem: LoadCollections, newItem: LoadCollections): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: LoadCollections,
        newItem: LoadCollections
    ): Boolean =
        oldItem == newItem
}

class MyViewHolder(val binding: CollectionPhotoSimpleBinding) :
    RecyclerView.ViewHolder(binding.root)