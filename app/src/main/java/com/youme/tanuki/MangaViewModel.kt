package com.youme.tanuki

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaViewModel2(
    private val repository: MangaRepository = MangaRepository()
) : ViewModel() {
    private val _mangaDetails = MutableStateFlow<MangaDetails?>(null)
    val mangaDetails: StateFlow<MangaDetails?> = _mangaDetails.asStateFlow()
    private val _searchResults = MutableStateFlow<List<Manga>>(emptyList())
    val searchResults: StateFlow<List<Manga>> = _searchResults.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    fun fetchMangaDetails(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.fetchMangaDetails(name)
                .onSuccess { details ->
                    _mangaDetails.value = details
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
    fun searchMangas(query: String, page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.searchMangas(query, page)
                .onSuccess { results ->
                    _searchResults.value = results
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}