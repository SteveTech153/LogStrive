package com.example.logstrive.presentation.viewModel

import android.app.Application
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logstrive.util.SessionManager
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.data.repository.HabitRepository

class HabitViewModel(application: Application, private val repository: HabitRepository) : AndroidViewModel(application) {

    private val _userId: Int = getUserIdFromSharedPreferences()

    // LiveData to observe habits
    val allHabits: LiveData<List<Habit>> = repository.getAllHabitsForUser(_userId)

    // Function to add a habit
    fun addHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(habit)
        }
    }

    suspend fun getHabitsCountOfUser(): Int{
        return repository.getHabitsCountOfUser(SessionManager.getId(getApplication()))
    }

    suspend fun insertHabitLog(habitLog: HabitLog){
        repository.insertHabitLog(habitLog)
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(habit)
        }
    }

    fun clearAllHabits(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllHabitsForUser(userId)
        }
    }
    fun clearAllLogsOfUserOn(userId: Int, date: Long){
        repository.clearAllLogsOfUserOn(userId, date)
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

    suspend fun getIdOfACategory(categoryName: String): Int {
        return repository.getIdOfACategory(categoryName)
    }

    private fun getUserIdFromSharedPreferences(): Int {
        return SessionManager.getId(getApplication())
    }

    suspend fun getHabitLogsForUserOn(userId: Int, date: Long): LiveData<List<HabitLog>> {

            return repository.getHabitLogsForUserOn(userId, date)

    }

    fun getAllHabitsOfUser(userId: Int): LiveData<List<Habit>> {
        return repository.getAllHabitsForUser(userId)
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

