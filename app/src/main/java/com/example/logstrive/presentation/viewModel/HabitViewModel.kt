package com.example.logstrive.presentation.viewModel

import android.app.Application
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logstrive.util.SessionManager
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.entity.GraphItem
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.data.repository.HabitRepository
import com.example.logstrive.util.Helper
import com.example.logstrive.util.Helper.calculateDaysBetween
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HabitViewModel(application: Application, private val repository: HabitRepository) : AndroidViewModel(application) {

    private val _userId: Int = getUserIdFromSharedPreferences()

    private val _habitsCount = MutableLiveData<Int>()
    val habitsCount : LiveData<Int>
        get() = _habitsCount

    val allHabits: LiveData<List<Habit>> = repository.getAllHabitsForUser(_userId)

    private var _graphItems = MutableLiveData<List<GraphItem>>()
    val graphItems: LiveData<List<GraphItem>>
        get() = _graphItems

    val todaysHabitLogsOfUser: LiveData<List<HabitLog>>
        get() =  getTodaysHabitLogsForUserOn(SessionManager.getId(getApplication()))

    init {
        getHabitsCountOfUser()
    }

    fun addHabit(habit: Habit, callback: (Boolean)-> Unit) {
        viewModelScope.launch {
            if(getAllHabitNames(SessionManager.getId(getApplication())).contains(habit.habitName.lowercase())){
                callback(false)
            }else {
                repository.insert(habit)
                callback(true)
            }
        }
    }

     fun getHabitsCountOfUser() {
         viewModelScope.launch {
            _habitsCount.postValue( repository.getHabitsCountOfUser(SessionManager.getId(getApplication())) )
         }
    }

     fun insertHabitLog(habitLog: HabitLog){
         viewModelScope.launch {
            repository.insertHabitLog(habitLog)
         }
    }
    suspend fun getAllHabitNames(userId: Int): List<String>{
        return repository.getAllHabitNames(userId)
    }
     suspend fun getDailyLog(userId: Int, date: Long): DailyLog?{
        return repository.getDailyLog(userId, date)
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

     fun insertDailyLog(dailyLog: DailyLog){
         viewModelScope.launch {
            repository.insertDailyLog(dailyLog)
         }
    }

    fun clearAllHabits(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllHabitsForUser(userId)
        }
    }
    fun clearAllLogsOfUserOn(userId: Int, date: Long){
        viewModelScope.launch {
            repository.clearAllLogsOfUserOn(userId, date)
        }
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }

    private fun getUserIdFromSharedPreferences(): Int {
        return SessionManager.getId(getApplication())
    }

    private fun getHabitLogsForUserOnAsLiveData(userId: Int, date: Long): LiveData<List<HabitLog>>{
        return repository.getHabitLogsForUserOnAsLiveData(userId, date)
    }

    suspend fun getHabitLogsForUserOn(userId: Int, date: Long): List<HabitLog>{
        return repository.getHabitLogsForUserOn(userId, date)
    }

//    private fun getTodaysHabitLogsForUserOn(userId: Int) {
//        val source = repository.getHabitLogsForUserOnAsLiveData(userId, Helper.convertDateToLong(Date()))
//        _todaysLogsOfUser.addSource(source) { logs ->
//            _todaysLogsOfUser.value = logs
//        }
//    }
    private fun getTodaysHabitLogsForUserOn(userId: Int): LiveData<List<HabitLog>> {
        return repository.getHabitLogsForUserOnAsLiveData(userId, Helper.convertDateToLong(Date()))

    }

    fun getAllHabitsOfUser(userId: Int): LiveData<List<Habit>> {
        return repository.getAllHabitsForUser(userId)
    }

     fun getGraphItemsForUserForHabitsFrom(habits: List<Habit>, accountCreatedDate: Long) {
         viewModelScope.launch {
        val graphItems = mutableListOf<GraphItem>()
        val userId = SessionManager.getId(getApplication())

        // Calculate total days from account creation date to today
        val today = Calendar.getInstance().timeInMillis
        val totalDays = calculateDaysBetween(accountCreatedDate, today)

        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

        habits.forEach { habit ->
            val activeDates = getAllActiveDatesForHabitOf(habit.habitId, userId) //active days

            val noOfBoxes = if (totalDays > 28) totalDays+1 else 28
            val activeIndexes = activeDates?.map { date ->
                val index = calculateDaysBetween(accountCreatedDate, date)
                index
            }?.filter { it < noOfBoxes }

            val startYearMonth = dateFormat.format(Date(accountCreatedDate))
            val endYearMonth = dateFormat.format(Date(today))

            val graphItem = GraphItem(
                habitName = habit.habitName,
                noOfBoxes = noOfBoxes,
                activeIndexes = activeIndexes!!,
                startYearMonth = startYearMonth,
                endYearMonth = endYearMonth
            )
            graphItems.add(graphItem)
        }
            _graphItems.postValue(graphItems)
         }
    }

     private suspend fun getAllActiveDatesForHabitOf(habitId: Int, userId: Int): List<Long>? {
        return repository.getAllActiveDatesForHabitOf(habitId, userId)
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

