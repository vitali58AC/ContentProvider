package com.example.contentprovider.data

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class User(
    val id: Long,
    val name: String,
    val age: Int
) {
    companion object {
        val defaultUsers = listOf(
            User(1L, "Vasia", 19),
            User(2L, "Vania", 20),
            User(3L, "Vitali", 30),
            User(4L, "Albert", 40),
            User(5L, "Viktoria", 5),
            User(6L, "Alena", 29),
            User(7L, "Egor", 1),
            User(8L, "Ekaterina", 60)
        )
    }
}
