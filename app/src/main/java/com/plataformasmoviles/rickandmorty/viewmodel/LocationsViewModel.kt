package com.plataformasmoviles.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.toModel
import com.plataformasmoviles.rickandmorty.model.Location
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class LocationFilter { ALL, FAVORITES, NOT_FAVORITES }

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val filter: LocationFilter = LocationFilter.ALL,
    val hasError: Boolean = false
)

class LocationsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).locationDao()
    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState> = _uiState.asStateFlow()
    private var job: Job? = null

    init { loadLocations() }

    fun loadLocations() {
        job?.cancel()
        job = viewModelScope.launch {
            try {
                dao.getAllLocations().collect { entities ->
                    val selected = _uiState.value.filter
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, hasError = false,
                        favoriteIds = entities.filter { it.isFavorite }.map { it.id }.toSet(),
                        data = entities.filter {
                            when (selected) {
                                LocationFilter.ALL -> true
                                LocationFilter.FAVORITES -> it.isFavorite
                                LocationFilter.NOT_FAVORITES -> !it.isFavorite
                            }
                        }.map { it.toModel() }
                    )
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.value = _uiState.value.copy(isLoading = false, hasError = true) }
        }
    }

    fun setFilter(filter: LocationFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadLocations()
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            val item = dao.getLocationById(id).first() ?: return@launch
            dao.updateFavorite(id, !item.isFavorite)
        }
    }

    fun simulateError() {
        if (!_uiState.value.isLoading) return
        job?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }
}
