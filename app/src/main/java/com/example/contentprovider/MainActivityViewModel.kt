package com.example.contentprovider

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentprovider.data.Contact
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MainActivityRepository(application)

    val contacts = mutableStateListOf<Contact>()
    val isLoading = mutableStateOf(true)

    private fun updateContacts(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
    }


    fun getProfile(id: Long?): Contact {
        return try {
            contacts.first { it.id == id }
        } catch (t: RuntimeException) {
            contacts[0]
        }
    }

    fun deleteViewModelListOfContacts(id: Long) {
        val contact = contacts.first { it.id == id }
        contacts.remove(contact)
    }

    fun loadContacts() {
        viewModelScope.launch {
            try {
                updateContacts(repository.getAllContacts())
            } catch (t: Throwable) {
                Log.e("viewModel", "Problem with download contacts: $t")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            repository.deleteContact(id)
        }
    }

    fun saveContact(name: String, phone: String, email: String) {
        viewModelScope.launch {
            try {
                repository.saveContact(name, phone, email) { id ->
                    contacts.add(Contact(id, name, listOf(phone), listOf(email)))
                }
            } catch (t: Throwable) {
                Log.e("viewModel", "catch three $t")
            }
        }
    }

}