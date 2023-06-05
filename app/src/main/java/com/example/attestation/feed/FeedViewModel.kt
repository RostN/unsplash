package com.example.attestation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.attestation.AppDatabase
import com.example.attestation.FeedPhoto
import kotlinx.coroutines.flow.Flow

class FeedViewModel(
    private val feedDatabase: AppDatabase,

    ) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val pagedPhoto: Flow<PagingData<FeedPhoto>> =
        when (searchRequest) {
            "" -> {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        feedDatabase.getFeedPhotoDao().getAll()
                    },
                    remoteMediator = FeedRemoteMediator(
                        feedDatabase,
                    )
                ).flow.cachedIn(viewModelScope)
            }
            else -> {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = { FeedPhotoPagingSource() }).flow.cachedIn(viewModelScope)
            }
        }
}