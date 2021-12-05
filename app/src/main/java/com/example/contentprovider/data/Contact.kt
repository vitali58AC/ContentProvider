package com.example.contentprovider.data

data class Contact(
    val id: Long,
    val name: String,
    val number: List<String>,
    val email: List<String>
)
