package com.example.contentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.example.contentprovider.data.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivityRepository(private val context: Context) {


    suspend fun getAllContacts() = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use {
            getContactFromCursor(it)
        }.orEmpty()
    }

    private fun getContactFromCursor(cursor: Cursor): List<Contact> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Contact>()
        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex).orEmpty()
            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)
            //Номера хранятся в другой таблице
            list.add(
                Contact(
                    id = id,
                    name = name,
                    number = getPhonesForContact(id),
                    email = getEmailForContact(id)
                )
            )
        } while (cursor.moveToNext())
        return list
    }

    private fun getPhonesForContact(contactId: Long): List<String> {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getPhonesFromCursor(it)
        }.orEmpty()
    }

    private fun getPhonesFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<String>()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val number = cursor.getString(numberIndex)
            list.add(number)
        } while (cursor.moveToNext())
        return list
    }

    private fun getEmailForContact(contactId: Long): List<String> {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getEmailFromCursor(it)
        }.orEmpty()
    }

    private fun getEmailFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<String>()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            val number = cursor.getString(numberIndex)
            list.add(number)
        } while (cursor.moveToNext())
        return list
    }

    suspend fun deleteContact(id: Long) = withContext(Dispatchers.IO) {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id)
        context.contentResolver.delete(
            contactUri,
            null,
            null
        )
    }

    suspend fun saveContact(name: String, phone: String, email: String, sendId: (Long) -> Unit) =
        withContext(Dispatchers.IO) {
            val id = saveRawContact()
            saveContactName(id, name)
            saveContactPhone(id, phone)
            saveContactEmail(id, email)
            sendId(id)
        }

    private fun saveRawContact(): Long {
        val uri = context.contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            ContentValues()
        )
        Log.e("Repository", "uri= $uri")
        return uri?.lastPathSegment?.toLongOrNull() ?: error("Can't save raw contact")
    }

    private fun saveContactName(id: Long, name: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, id)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactPhone(id: Long, phone: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, id)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)

        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactEmail(id: Long, email: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, id)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }
}