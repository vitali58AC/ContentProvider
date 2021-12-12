package com.example.contentprovider.compose.costume_provider

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.contentprovider.data.Course

@Composable
fun CourseDetailScreen(course: Course) {
    Text(text = "Detail course screen $course")
}