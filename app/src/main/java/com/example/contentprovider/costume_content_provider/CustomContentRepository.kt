package com.example.contentprovider.costume_content_provider

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.core.net.toUri
import com.example.contentprovider.data.Course
import com.example.contentprovider.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomContentRepository(private val context: Context) {

    private val uriUser = CustomContentProvider.USER_URI.toUri()
    private val uriCourse = CustomContentProvider.COURSE_URI.toUri()

    suspend fun saveDefaultUsers() = withContext(Dispatchers.IO) {
        ContentValues().apply {
            User.defaultUsers.map {
                put(CustomContentProvider.COLUMN_USER_ID, it.id)
                put(CustomContentProvider.COLUMN_USER_NAME, it.name)
                put(CustomContentProvider.COLUMN_USER_AGE, it.age)
                context.contentResolver.insert(
                    uriUser,
                    this
                )
            }
        }
    }

    suspend fun saveDefaultCourses() = withContext(Dispatchers.IO) {
        ContentValues().apply {
            Course.defaultCourses.map {
                put(CustomContentProvider.COLUMN_COURSE_ID, it.id)
                put(CustomContentProvider.COLUMN_COURSE_TITLE, it.title)
                context.contentResolver.insert(
                    uriCourse,
                    this
                )
            }
        }
    }

    suspend fun getAllUser() = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            uriUser,
            null,
            null,
            null,
            null
        )?.use {
            getUserFromCursor(it)
        }.orEmpty()
    }

    private fun getUserFromCursor(cursor: Cursor): List<User> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<User>()
        do {
            val nameIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_USER_NAME)
            val name = cursor.getString(nameIndex).orEmpty()
            val idIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_USER_ID)
            val id = cursor.getLong(idIndex)
            val ageIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_USER_AGE)
            val age = cursor.getInt(ageIndex)
            list.add(User(id, name, age))
        } while (cursor.moveToNext())
        return list
    }

    suspend fun getAllCourses() = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            uriCourse,
            null,
            null,
            null,
            null
        )?.use {
            getCoursesFromCursor(it)
        }.orEmpty()
    }

    private fun getCoursesFromCursor(cursor: Cursor): List<Course> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Course>()
        do {
            val titleIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_COURSE_TITLE)
            val title = cursor.getString(titleIndex).orEmpty()
            val idIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_COURSE_ID)
            val id = cursor.getLong(idIndex)
            list.add(Course(id, title))
        } while (cursor.moveToNext())
        return list
    }

    fun getUserFromId(id: Long): Long? {
        return context.contentResolver.query(
            uriUser,
            null,
            "${CustomContentProvider.COLUMN_USER_ID} = ?",
            arrayOf(id.toString()),
            null
        )?.use {
            getIdFromCursor(it)
        }?.or(-1L)
    }

    private fun getIdFromCursor(cursor: Cursor): Long {
        if (cursor.moveToFirst().not()) return -1L
        var id: Long
        do {
            val idIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_USER_ID)
            val userId = cursor.getString(idIndex)
            id = userId.toLong()
        } while (cursor.moveToNext())
        return id
    }

}