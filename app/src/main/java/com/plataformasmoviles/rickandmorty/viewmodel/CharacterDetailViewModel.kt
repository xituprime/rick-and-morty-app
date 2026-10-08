package com.plataformasmoviles.rickandmorty.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.data.CharacterRepository
import com.plataformasmoviles.rickandmorty.model.Character
import com.plataformasmoviles.rickandmorty.navigation.CharacterDetailDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailDestination>().id

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()
        _uiState.value = CharacterDetailUiState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(2000)

            val character = CharacterRepository.getCharacterById(characterId)

            _uiState.value = CharacterDetailUiState(
                isLoading = false,
                data = character,
                hasError = character == null
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