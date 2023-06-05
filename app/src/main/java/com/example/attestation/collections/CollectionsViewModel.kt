package com.example.attestation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.attestation.LoadCollections
import com.example.attestation.LoadPhotoResponse
import kotlinx.coroutines.flow.Flow

class CollectionsViewModel : ViewModel() {
    lateinit var id: String

    val pagedPhoto: Flow<PagingData<LoadCollections>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { CollectionsPagingSource() }).flow.cachedIn(viewModelScope)

    val pagedPhotoCollection: Flow<PagingData<LoadPhotoResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { CollectionsPhotoPagingSource(id) }).flow.cachedIn(viewModelScope)
}