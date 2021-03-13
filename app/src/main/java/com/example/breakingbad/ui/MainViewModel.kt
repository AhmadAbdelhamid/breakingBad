package com.example.breakingbad.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.breakingbad.repo.RemoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import com.example.model.Character


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RemoteRepo,
    private val appScope: CoroutineScope
) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()
    private val allCharacters: LiveData<PagingData<Character>> =
        Transformations.switchMap(reloadTrigger) {
            repo.getAllCharacters().cachedIn(viewModelScope)
        }

    init { refreshCharacters() }

    fun getCharacters(): LiveData<PagingData<Character>> = allCharacters.cachedIn(viewModelScope)

    private fun refreshCharacters() { reloadTrigger.value = true }

}