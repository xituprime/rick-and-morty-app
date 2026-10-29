package com.plataformasmoviles.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.toModel
import com.plataformasmoviles.rickandmorty.model.Character
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val favorites: Set<Int> = emptySet(),
    val showOnlyFavorites: Boolean = false
)


class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val characterDao =
        AppDatabase.getDatabase(application).characterDao()

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacters()
    }


    fun loadCharacters() {
        loadingJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            hasError = false
        )

        loadingJob = viewModelScope.launch {
            try {
                characterDao.getAllCharacters().collect { entities ->

                    val favoriteIds = entities
                        .filter { it.isFavorite }
                        .map { it.id }
                        .toSet()

                    val currentFilter =
                        _uiState.value.showOnlyFavorites

                    val visibleCharacters = if (currentFilter) {
                        entities.filter { it.isFavorite }
                    } else {
                        entities
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        data = visibleCharacters.map { it.toModel() },
                        favorites = favoriteIds,
                        hasError = false
                    )
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    throw e
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }


    fun toggleFavorite(characterId: Int) {
        viewModelScope.launch {
            val entity = characterDao
                .getCharacterById(characterId)
                .first() ?: return@launch

            characterDao.updateFavorite(
                id = characterId,
                isFavorite = !entity.isFavorite
            )
        }
    }

    fun toggleFavoritesFilter() {
        _uiState.value = _uiState.value.copy(
            showOnlyFavorites = !_uiState.value.showOnlyFavorites
        )

        loadCharacters()
    }

    fun simulateError() {
        if (!_uiState.value.isLoading) return

        loadingJob?.cancel()

        _uiState.value = CharactersUiState(
            isLoading = false,
            hasError = true
        )
    }
}
