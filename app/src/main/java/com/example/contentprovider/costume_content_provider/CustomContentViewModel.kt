package com.example.contentprovider.costume_content_provider

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentprovider.data.Course
import com.example.contentprovider.data.User
import kotlinx.coroutines.launch

class CustomContentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CustomContentRepository(application)

    val users = mutableStateListOf<User>()
    val courses = mutableStateListOf<Course>()
    private val firstScreenLaunchCheck = mutableStateOf(false)
    val errorWithGetFromId = mutableStateOf(false)

    fun getUser(id: Long?): User {
        return try {
            users.first { it.id == id }
        } catch (t: RuntimeException) {
            users[0]
        }
    }

    fun getCourse(id: Long?): Course {
        return try {
            courses.first { it.id == id }
        } catch (t: RuntimeException) {
            courses[0]
        }
    }

    fun saveDefaultTables() {
        viewModelScope.launch {
            repository.saveDefaultUsers()
            repository.saveDefaultCourses()
        }
    }

    fun getAllUserAndCourses() {
        viewModelScope.launch {
            try {
                if (firstScreenLaunchCheck.value.not()) {
                    users.addAll(repository.getAllUser())
                    courses.addAll(repository.getAllCourses())
                }
            } catch (t: Throwable) {
                Log.e("contentViewModel", "t is $t, message is ${t.message}")
            } finally {
                firstScreenLaunchCheck.value = true
            }
        }
    }

    fun getUserFromId(id: Long) {
        viewModelScope.launch {
            try {
                if (repository.getUserFromId(id) != -1L) {
                    errorWithGetFromId.value = true
                }
            } finally {
                //Далее нужно сделать кнопку для поиска пользователя по Id и реализовать её
                    //DO IT!
                errorWithGetFromId.value = false
            }
        }
    }

}