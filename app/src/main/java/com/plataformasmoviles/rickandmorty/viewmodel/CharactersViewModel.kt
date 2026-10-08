package com.plataformasmoviles.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plataformasmoviles.rickandmorty.data.CharacterRepository
import com.plataformasmoviles.rickandmorty.model.Character
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadingJob?.cancel()

        _uiState.value = CharactersUiState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(4000)

            _uiState.value = CharactersUiState(
                isLoading = false,
                data = CharacterRepository.characters,
                hasError = false
            )
        }
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