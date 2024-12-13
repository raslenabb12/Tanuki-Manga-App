package com.youme.tanuki

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
class MangaViewModel : ViewModel() {
    val mangaFlow: Flow<PagingData<Manga>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MangaPagingSource() }
    ).flow.cachedIn(viewModelScope)
}
