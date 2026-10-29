package com.plataformasmoviles.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.toModel
import com.plataformasmoviles.rickandmorty.model.Character
import com.plataformasmoviles.rickandmorty.navigation.CharacterDetailDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false,
    val isFavorite: Boolean = false
)

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterDao =
        AppDatabase.getDatabase(application).characterDao()

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailDestination>().id

    private val _uiState =
        MutableStateFlow(CharacterDetailUiState())

    val uiState: StateFlow<CharacterDetailUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()

        _uiState.value = CharacterDetailUiState(
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            try {
                characterDao.getCharacterById(characterId)
                    .collect { entity ->

                        _uiState.value = CharacterDetailUiState(
                            isLoading = false,
                            data = entity?.toModel(),
                            hasError = entity == null,
                            isFavorite = entity?.isFavorite ?: false
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = CharacterDetailUiState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value

        if (currentState.data == null) return

        viewModelScope.launch {
            characterDao.updateFavorite(
                id = characterId,
                isFavorite = !currentState.isFavorite
            )
        }
    }

    fun simulateError() {
        if (!_uiState.value.isLoading) return

        loadingJob?.cancel()

        _uiState.value = CharacterDetailUiState(
            isLoading = false,
            hasError = true
        )
    }
}
