package com.example.myapplication

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

class ResturantCP() : ContentProvider() {
    //public
    companion object {
        val PROVIDER_NAME = "com.example.MyApplication.ResturantCP"
        val URL = "content://" + PROVIDER_NAME + "/restrurant"
        val CONTENT_URI = Uri.parse(URL)
        val _ID = "_id"
        val NAME = "name"
        private val STUDENTS_PROJECTION_MAP: HashMap<String, String>? = null
        val Customer= 1
        val STUDENT_ID = 2
        val uriMatcher: UriMatcher? = null
        val DATABASE_NAME = "College"
        val STUDENTS_TABLE_NAME = "students"
        val DATABASE_VERSION = 1
        val CREATE_DB_TABLE = " CREATE TABLE " + STUDENTS_TABLE_NAME +        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL, " +
                " grade TEXT NOT NULL);"
        private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH);
        init
        {
            sUriMatcher.addURI(PROVIDER_NAME, "students", STUDENTS);
            sUriMatcher.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
        }
    }
    private var db: SQLiteDatabase? = null  //private
    //internal class
    private class DatabaseHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + STUDENTS_TABLE_NAME)
            onCreate(db)
        }
    }

    //6 functions
    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.  */
        db = dbHelper.writableDatabase
        return if (db == null) false else true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        /**
         * Add a new student record
         */
        val rowID = db!!.insert(STUDENTS_TABLE_NAME, "", values)
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLException("Failed to add a record into $uri")
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        qb.tables = STUDENTS_TABLE_NAME
        if (uriMatcher != null) {
            when (uriMatcher.match(uri)) {
                /* STUDENTS -> qb.projectionMap =
                    STUDENTS_PROJECTION_MAP */
                STUDENT_ID -> qb.appendWhere(_ID + "=" + uri.pathSegments[1])
                else -> {null
                }
            }
        }


        if (sortOrder == null || sortOrder === "") {
            /**
             * By default sort on student names
             */
            sortOrder = NAME
        }
        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        /**
         * register to watch a content URI for changes  */
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            STUDENTS -> count = db!!.delete(
                STUDENTS_TABLE_NAME, selection,
                selectionArgs
            )
            STUDENT_ID -> {
                val id = uri.pathSegments[1]
                count = db!!.delete(
                    STUDENTS_TABLE_NAME,
                    _ID + " = " + id +
                            if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?  ): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            STUDENTS -> count = db!!.update(
                STUDENTS_TABLE_NAME, values, selection,
                selectionArgs
            )
            STUDENT_ID -> count = db!!.update(
                STUDENTS_TABLE_NAME,
                values,
                _ID + " = " + uri.pathSegments[1] + (if (!TextUtils.isEmpty(selection)) " AND ($selection)" else ""),
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher!!.match(uri)) {
            STUDENTS -> return "These info about students"
            STUDENT_ID -> return "This info about specific studentstudents"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}