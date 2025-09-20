package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.Database

data class MAlumno(
    var registro: String = "",
    var apellidop: String = "",
    var apellidom: String = "",
    var nombre: String = ""
) {
    // insertar(): boolean
    fun insertar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("registro", registro)
            put("apellidop", apellidop)
            put("apellidom", apellidom)
            put("nombre", nombre)
        }
        val newRowId = db.insert(Database.Companion.TABLE_ESTUDIANTE, null, values)
        db.close()
        return newRowId != -1L
    }

    // obtener(registro: String): MAlumno
    companion object {
        fun obtener(context: Context, registro: String): MAlumno? {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_ESTUDIANTE,
                arrayOf("registro", "apellidop", "apellidom", "nombre"),
                "registro = ?",
                arrayOf(registro),
                null, null, null
            )
            var alumno: MAlumno? = null
            if (cursor.moveToFirst()) {
                alumno = MAlumno(
                    cursor.getString(cursor.getColumnIndexOrThrow("registro")),
                    cursor.getString(cursor.getColumnIndexOrThrow("apellidop")),
                    cursor.getString(cursor.getColumnIndexOrThrow("apellidom")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                )
            }
            cursor.close()
            db.close()
            return alumno
        }

        // listar(): List<MAlumno>
        fun listar(context: Context): List<MAlumno> {
            val list = mutableListOf<MAlumno>()
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(Database.Companion.TABLE_ESTUDIANTE, arrayOf("registro", "apellidop", "apellidom", "nombre"), null, null, null, null, "registro ASC")
            if (cursor.moveToFirst()) {
                do {
                    val alumno = MAlumno(
                        cursor.getString(cursor.getColumnIndexOrThrow("registro")),
                        cursor.getString(cursor.getColumnIndexOrThrow("apellidop")),
                        cursor.getString(cursor.getColumnIndexOrThrow("apellidom")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                    )
                    list.add(alumno)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }
    }

    // actualizar(): boolean
    fun actualizar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("apellidop", apellidop)
            put("apellidom", apellidom)
            put("nombre", nombre)
        }
        val rows = db.update(Database.Companion.TABLE_ESTUDIANTE, values, "registro = ?", arrayOf(registro))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val rows = db.delete(Database.Companion.TABLE_ESTUDIANTE, "registro = ?", arrayOf(registro))
        db.close()
        return rows > 0
    }
}