package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.Database

data class MGrupo(var id: Int = 0, var nombre: String = "", var id_materia: Int = 0) {

    fun insertar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_materia", id_materia)
        }
        val newRowId = db.insert(Database.TABLE_GRUPO, null, values)
        db.close()
        return newRowId != -1L
    }

    companion object {
        fun obtener(context: Context, id: Int): MGrupo? {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.TABLE_GRUPO,
                arrayOf("id", "nombre", "id_materia"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var grupo: MGrupo? = null
            if (cursor.moveToFirst()) {
                grupo = MGrupo(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_materia"))
                )
            }
            cursor.close()
            db.close()
            return grupo
        }

        fun listar(context: Context): List<MGrupo> {
            val list = mutableListOf<MGrupo>()
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.TABLE_GRUPO,
                arrayOf("id", "nombre", "id_materia"), 
                null, null, null, null, "id DESC"
            )
            if (cursor.moveToFirst()) {
                do {
                    val g = MGrupo(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_materia"))
                    )
                    list.add(g)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }

        fun obtenerMateria(context: Context, idMateria: Int): String {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.TABLE_MATERIA,
                arrayOf("nombre"),
                "id = ?",
                arrayOf(idMateria.toString()),
                null, null, null
            )
            var nombreMateria = "Materia no encontrada"
            if (cursor.moveToFirst()) {
                nombreMateria = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            }
            cursor.close()
            db.close()
            return nombreMateria
        }
    }

    fun actualizar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_materia", id_materia)
        }
        val rows = db.update(Database.TABLE_GRUPO, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    fun eliminar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val rows = db.delete(Database.TABLE_GRUPO, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}
