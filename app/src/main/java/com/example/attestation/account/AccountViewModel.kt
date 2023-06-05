package com.example.attestation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.attestation.LoadPhotoResponse
import kotlinx.coroutines.flow.Flow

class AccountViewModel : ViewModel() {
    lateinit var id: String
    val pagedPhoto: Flow<PagingData<LoadPhotoResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { AccountPhotoPagingSource(id) }).flow.cachedIn(viewModelScope)
}