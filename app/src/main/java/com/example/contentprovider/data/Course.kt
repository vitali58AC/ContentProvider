package com.example.contentprovider.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Course(
    val id: Long,
    val title: String
) {
    companion object {
         val defaultCourses = listOf(
            Course(1L, "Android - developer 2022"),
            Course(2L, "Kotlin basic"),
            Course(3L, "Kotlin developer"),
            Course(4L, "Java basic"),
            Course(5L, "Java developer")
        )
    }
}
