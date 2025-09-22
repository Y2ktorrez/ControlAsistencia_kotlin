package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.Database

data class MClase(
    var id: Int = 0,
    var fecha: String = "",
    var hora_inicio: String = "",
    var hora_fin: String = "",
    var estado: String = "",
    var id_grupo: Int = 0
) {
    // insertar(): boolean
    fun insertar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("fecha", fecha)
            put("hora_inicio", hora_inicio)
            put("hora_fin", hora_fin)
            put("estado", estado)
            put("id_grupo", id_grupo)
        }
        val newRowId = db.insert(Database.Companion.TABLE_CLASE, null, values)
        db.close()
        return newRowId != -1L
    }

    // obtener(id: int): MClase
    companion object {
        fun obtener(context: Context, id: Int): MClase? {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_CLASE,
                arrayOf("id", "fecha", "hora_inicio", "hora_fin", "estado", "id_grupo"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var clase: MClase? = null
            if (cursor.moveToFirst()) {
                clase = MClase(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hora_fin")),
                    cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_grupo"))
                )
            }
            cursor.close()
            db.close()
            return clase
        }

        // listar(): List<MClase>
        fun listar(context: Context): List<MClase> {
            val list = mutableListOf<MClase>()
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_CLASE,
                arrayOf("id", "fecha", "hora_inicio", "hora_fin", "estado", "id_grupo"),
                null, null, null, null, "id DESC"
            )
            if (cursor.moveToFirst()) {
                do {
                    val c = MClase(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hora_fin")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_grupo"))
                    )
                    list.add(c)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }

        // obtenerGrupo(): String - Obtiene el nombre del grupo asociado
        fun obtenerGrupo(context: Context, idGrupo: Int): String {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_GRUPO,
                arrayOf("nombre"),
                "id = ?",
                arrayOf(idGrupo.toString()),
                null, null, null
            )
            var nombreGrupo = "Grupo no encontrado"
            if (cursor.moveToFirst()) {
                nombreGrupo = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            }
            cursor.close()
            db.close()
            return nombreGrupo
        }
    }

    // actualizar(): boolean
    fun actualizar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("fecha", fecha)
            put("hora_inicio", hora_inicio)
            put("hora_fin", hora_fin)
            put("estado", estado)
            put("id_grupo", id_grupo)
        }
        val rows = db.update(Database.Companion.TABLE_CLASE, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val rows = db.delete(Database.Companion.TABLE_CLASE, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}