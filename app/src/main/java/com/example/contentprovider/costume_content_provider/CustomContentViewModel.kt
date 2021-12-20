package com.example.contentprovider.costume_content_provider

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentprovider.data.Course
import com.example.contentprovider.data.User
import kotlinx.coroutines.*

class CustomContentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CustomContentRepository(application)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    val users = mutableStateListOf<User>()
    val courses = mutableStateListOf<Course>()
    private val firstScreenLaunchCheck = mutableStateOf(false)
    val errorWithGetFromId = mutableStateOf("")
    val addDeleteOperationResult = mutableStateOf("")

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
        scope.launch {
            repository.saveDefaultUsers()
            repository.saveDefaultCourses()
        }
    }

    fun getAllUserAndCourses() {
        scope.launch {
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

    fun getUserFromId(id: String, callBack: (Long) -> Unit, error: () -> Unit) {
        viewModelScope.launch {
            try {
                val resultId = repository.getUserFromId(id.toLong())
                if (resultId != -1L) {
                    callBack(resultId)
                } else {
                    errorWithGetFromId.value = "No found"
                    error()
                }
            } catch (t: NumberFormatException) {
                errorWithGetFromId.value = "Incorrect id"
                error()
            } catch (t: Throwable) {
                errorWithGetFromId.value = "Error with search user"
                error()
            }
        }
    }

    fun saveNewUser(userString: String, toast: () -> Unit) {
        addDeleteOperationResult.value = ""
        viewModelScope.launch {
            try {
                val user = userString.split(" ").let {
                    User(
                        it.first().toLong(),
                        it.drop(1).dropLast(1).joinToString(" "),
                        it.last().toInt()
                    )
                }
                repository.saveOneUser(user)
                users.add(user)
                addDeleteOperationResult.value = "Success added"
                toast()
            } catch (t: Throwable) {
                Log.e("Custom_view_model", "Error with add new user ${t.message}")
                addDeleteOperationResult.value = "Error: ${t.message}"
                toast()
            }
        }
    }

    fun deleteUserFromId(idString: String, toast: () -> Unit) {
        addDeleteOperationResult.value = ""
        viewModelScope.launch {
            Log.e("Custom_view_model", Thread.currentThread().name)
            try {
                val id = idString.trim().toLong()
                val deleteResult = repository.deleteUserFromId(id)
                if (deleteResult == 1) {
                    deleteUserFromUsersList(id)
                    addDeleteOperationResult.value = "Success deleted"
                } else {
                    addDeleteOperationResult.value = "No such id"
                }
                toast()
            } catch (t: Throwable) {
                Log.e("Custom_view_model", "Error with delete user ${t.message}")
                addDeleteOperationResult.value = "Error: ${t.message}"
                toast()
            }
        }
    }

    private fun deleteUserFromUsersList(id: Long) {
        try {
            val user = users.first { it.id == id }
            users.remove(user)
        } catch (t: Throwable) {
            Log.e("Custom_view_model", "Error with remove user from users list ${t.message}")
        }
    }

    fun deleteAllUsers(toast: () -> Unit) {
        addDeleteOperationResult.value = ""
        scope.launch {
            try {
                for (user in users) {
                    repository.deleteUserFromId(user.id)
                }
                addDeleteOperationResult.value = "All users success deleted"
                users.clear()
                toast()
            } catch (t: Throwable) {
                addDeleteOperationResult.value = "Error with delete all users ${t.message}"
                toast()
            }
        }
    }

    fun updateUserById(userString: String, callBack: () -> Unit) {
        addDeleteOperationResult.value = ""
        viewModelScope.launch {
            try {
                val user = userString.split(" ")
                val resultId = repository.getUserFromId(user.first().toLong())
                val newUser = User(
                    user.first().toLong(),
                    user.subList(1, user.lastIndex).joinToString(" "),
                    user.last().toInt()
                )
                if (resultId != -1L) {
                    repository.updateUserFromId(newUser.id, newUser.name, newUser.age)
                    val oldUser = users.first { it.id == user.first().toLong() }
                    users.remove(oldUser)
                    users.add(newUser)
                    addDeleteOperationResult.value = "User updated successfully"
                    callBack()
                } else {
                    addDeleteOperationResult.value = "User not found"
                    callBack()
                }
            } catch (t: Throwable) {
                addDeleteOperationResult.value = "User has not been updated"
                callBack()
            }
        }
    }


    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }


}