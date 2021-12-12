package com.example.contentprovider.costume_content_provider

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.contentprovider.BuildConfig
import com.example.contentprovider.data.Course
import com.example.contentprovider.data.User
import com.example.contentprovider.utils.Constants
import com.squareup.moshi.Moshi

class CustomContentProvider : ContentProvider() {

    private lateinit var userPrefs: SharedPreferences
    private lateinit var coursesPrefs: SharedPreferences

    private val userAdapter = Moshi.Builder().build().adapter(User::class.java)
    private val coursesAdapter = Moshi.Builder().build().adapter(Course::class.java)

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITIES, PATH_USERS, TYPE_USERS)
        addURI(AUTHORITIES, PATH_COURSES, TYPE_COURSES)
        //# любое число, * любой текст
        addURI(AUTHORITIES, "$PATH_USERS/#", TYPE_USER_ID)
        addURI(AUTHORITIES, "$PATH_COURSES/#", TYPE_COURSE_ID)
    }
    //Тут точно не null
    override fun onCreate(): Boolean {
        userPrefs = context!!.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE)
        coursesPrefs = context!!.getSharedPreferences(Constants.COURSE_PREFS, Context.MODE_PRIVATE)
        //Вернуть флаг, что значит инициализация прошла успешно
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return when (uriMatcher.match(p0)) {
            TYPE_USERS -> getAllUsers()
            TYPE_COURSES -> getAllCourses()
            else -> null
        }
    }

    private fun getAllUsers(): Cursor {
        val allUsers = userPrefs.all.mapNotNull {
            val userJsonString = it.value as String
            userAdapter.fromJson(userJsonString)
        }
        val cursor = MatrixCursor(arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_AGE))
        allUsers.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.name)
                .add(it.age)
        }
        return cursor
    }

    private fun getAllCourses(): Cursor {
        val allCourses = coursesPrefs.all.mapNotNull {
            val courseJsonString = it.value as String
            coursesAdapter.fromJson(courseJsonString)
        }
        val cursor = MatrixCursor(arrayOf(COLUMN_COURSE_ID, COLUMN_COURSE_TITLE))
        allCourses.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.title)
        }
        return cursor
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        p1 ?: return null
        return when (uriMatcher.match(p0)) {
            TYPE_USERS -> saveUser(p1)
            TYPE_COURSES -> saveCourse(p1)
            else -> null
        }
    }

    //О потоках нужно думать тому, кто использует провайдер
    @SuppressLint("ApplySharedPref")
    private fun saveUser(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_USER_ID) ?: return null
        val name = contentValues.getAsString(COLUMN_USER_NAME) ?: return null
        val age = contentValues.getAsInteger(COLUMN_USER_AGE) ?: return null
        val user = User(id, name, age)
        userPrefs.edit()
            .putString(id.toString(), userAdapter.toJson(user))
            .commit()
        return Uri.parse("content://$AUTHORITIES/$PATH_USERS/$id")
    }

    @SuppressLint("ApplySharedPref")
    private fun saveCourse(contentValues: ContentValues): Uri? {
        val id = contentValues.getAsLong(COLUMN_COURSE_ID) ?: return null
        val title = contentValues.getAsString(COLUMN_COURSE_TITLE) ?: return null
        val course = Course(id, title)
        coursesPrefs.edit()
            .putString(id.toString(), coursesAdapter.toJson(course))
            .commit()
        return Uri.parse("content://$AUTHORITIES/$PATH_COURSES/$id")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return when (uriMatcher.match(p0)) {
            TYPE_USERS -> deleteUser(p0)
            TYPE_COURSES -> deleteCourse(p0)
            else -> 0
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun deleteUser(uri: Uri): Int {
        val id = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        return if (userPrefs.contains(id)) {
            userPrefs.edit()
                .remove(id)
                .commit()
            1
        } else {
            0
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun deleteCourse(uri: Uri): Int {
        val id = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        return if (coursesPrefs.contains(id)) {
            coursesPrefs.edit()
                .remove(id)
                .commit()
            1
        } else {
            0
        }
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        p1 ?: return 0
        return when (uriMatcher.match(p0)) {
            TYPE_USERS -> updateUser(p0, p1)
            TYPE_COURSES -> updateCourse(p0, p1)
            else -> 0
        }
    }

    private fun updateUser(uri: Uri, contentValues: ContentValues): Int {
        val id = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        return if (userPrefs.contains(id)) {
            saveUser(contentValues)
            1
        } else {
            0
        }
    }

    private fun updateCourse(uri: Uri, contentValues: ContentValues): Int {
        val id = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        return if (coursesPrefs.contains(id)) {
            saveCourse(contentValues)
            1
        } else {
            0
        }
    }

    companion object {
        private const val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.provider"
        private const val PATH_USERS = "users"
        private const val PATH_COURSES = "courses"

        const val USER_URI = "content://$AUTHORITIES/$PATH_USERS"
        const val COURSE_URI = "content://$AUTHORITIES/$PATH_COURSES"

        private const val TYPE_USERS = 1
        private const val TYPE_COURSES = 2
        private const val TYPE_USER_ID = 3
        private const val TYPE_COURSE_ID = 4

        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_AGE = "age"

        const val COLUMN_COURSE_ID = "id"
        const val COLUMN_COURSE_TITLE = "title"
    }

}