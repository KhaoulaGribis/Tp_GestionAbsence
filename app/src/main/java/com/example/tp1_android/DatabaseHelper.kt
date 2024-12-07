
package com.example.tp1_android

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_database.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "etudiants"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ETAT = "etat"
        const val COLUMN_IMAGE_URI = "image_uri"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                 $COLUMN_ETAT TEXT NOT NULL,
                $COLUMN_IMAGE_URI TEXT
            )
        """
        db.execSQL(createTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun addEtudiant(name: String, etat: String, imageUri: Uri?): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_ETAT, etat)
            put(COLUMN_IMAGE_URI, imageUri?.toString())
        }
        return db.insert(TABLE_NAME, null, values)
    }
    fun updateEtudiantEtat(id: Long, newEtat: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("etat", newEtat)  // La colonne 'etat' est mise à jour
        }

        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        db.update(TABLE_NAME, values, selection, selectionArgs)
        db.close()
    }

    fun getAllEtudiants(): List<Etudiant> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_ETAT, COLUMN_IMAGE_URI), null, null, null, null, null)
        val etudiants = mutableListOf<Etudiant>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val etat = getString(getColumnIndexOrThrow(COLUMN_ETAT))
                val imageUriString = getString(getColumnIndexOrThrow(COLUMN_IMAGE_URI))
                val imageUri = if (imageUriString.isNullOrBlank()) null else Uri.parse(imageUriString)
                etudiants.add(Etudiant(id,name, etat, imageUri))
            }
        }
        cursor.close()
        return etudiants
    }


}