package com.example.logstrive

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HabitViewModel(application: Application, private val repository: HabitRepository) : AndroidViewModel(application) {

//    fun insertHabit(habit: Habit) {
//        viewModelScope.launch {
//            repository.insert(habit)
//        }
//    }
//
//    fun getAllHabitsForUser(userId: Int): Flow<List<Habit>> {
//        return repository.getAllHabitsForUser(userId)
//    }


    private val _userId: Int = getUserIdFromSharedPreferences()

    // LiveData to observe habits
    val allHabits: LiveData<List<Habit>> = liveData(Dispatchers.IO) {
        repository.getAllHabitsForUser(_userId).collect { habits ->
            emit(habits)
        }
    }

//    val allCategories: LiveData<List<Category>> = liveData(Dispatchers.IO) {
//        repository.getAllCategories()
//    }

    // Function to add a habit
    fun addHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(habit)
        }
    }

    fun updateHabit(habit: Habit){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(habit)
        }
    }

    fun deleteHabit(habit: Habit){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(habit)
        }
    }

    fun clearAllHabits(userId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllHabitsForUser(userId)
        }
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

    suspend fun getIdOfACategory(categoryName: String): Int{
        return repository.getIdOfACategory(categoryName)
    }

    private fun getUserIdFromSharedPreferences(): Int {
        return SessionManager.getId(getApplication())
    }
}

class HabitViewModelFactory(
    private val application: Application,
    private val repository: HabitRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
