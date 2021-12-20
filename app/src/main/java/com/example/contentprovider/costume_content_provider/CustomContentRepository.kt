package com.example.contentprovider.costume_content_provider

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
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
        Log.e("custom_repository", "auth ${CustomContentProvider.USER_URI}")
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

    fun getUserFromId(id: Long): Long {
        return context.contentResolver.query(
            //ContentProvider.USER_URI_SELECT.toUri(),
            uriUser,
            null,
            //Это не работает, потому что нет реализации
            "${CustomContentProvider.COLUMN_USER_ID} = ?",
            arrayOf(id.toString()),
            null
        )?.use {
            getIdFromCursor(it, id)
        } ?: -1
    }

    private fun getIdFromCursor(cursor: Cursor, targetId: Long): Long {
        val error = -1L
        if (cursor.moveToFirst().not()) return error
        do {
            val idIndex = cursor.getColumnIndex(CustomContentProvider.COLUMN_USER_ID)
            val userId = cursor.getString(idIndex)
            if (userId.toLong() == targetId) return userId.toLong()
        } while (cursor.moveToNext())
        return error
    }

    suspend fun saveOneUser(user: User) = withContext(Dispatchers.IO) {
        ContentValues().apply {
            put(CustomContentProvider.COLUMN_USER_ID, user.id)
            put(CustomContentProvider.COLUMN_USER_NAME, user.name)
            put(CustomContentProvider.COLUMN_USER_AGE, user.age)
            context.contentResolver.insert(
                uriUser,
                this
            )
        }
    }

    suspend fun deleteUserFromId(id: Long) = withContext(Dispatchers.IO) {
        val resultUri = ContentUris.withAppendedId(CustomContentProvider.USER_URI_PASS.toUri(), id)
        context.contentResolver.delete(
            resultUri,
            null,
            null
        )
    }


    suspend fun updateUserFromId(id: Long, name: String, age: Int) = withContext(Dispatchers.IO) {
        val resultUri = ContentUris.withAppendedId(CustomContentProvider.USER_URI_PASS.toUri(), id)
        val contentValues = ContentValues().apply {
            put(CustomContentProvider.COLUMN_USER_ID, id)
            put(CustomContentProvider.COLUMN_USER_NAME, name)
            put(CustomContentProvider.COLUMN_USER_AGE, age)
        }
        context.contentResolver.update(
            resultUri,
            contentValues,
            null,
            null
        )
    }

}