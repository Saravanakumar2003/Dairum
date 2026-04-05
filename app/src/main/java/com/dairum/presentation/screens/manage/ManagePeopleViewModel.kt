package com.dairum.presentation.screens.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dairum.domain.model.Person
import com.dairum.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagePeopleViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManagePeopleUiState())
    val uiState: StateFlow<ManagePeopleUiState> = _uiState.asStateFlow()

    init {
        loadPeople()
    }

    private fun loadPeople() {
        viewModelScope.launch {
            personRepository.getAllWithCountFlow()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { people ->
                    _uiState.update { it.copy(people = people, isLoading = false) }
                }
        }
    }

    fun createPerson(person: Person) {
        viewModelScope.launch {
            personRepository.create(person)
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            personRepository.update(person)
        }
    }

    fun deletePerson(id: Long) {
        viewModelScope.launch {
            personRepository.delete(id)
        }
    }

    fun toggleFavorite(id: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            personRepository.toggleFavorite(id, isFavorite)
        }
    }
}

data class ManagePeopleUiState(
    val people: List<Person> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
