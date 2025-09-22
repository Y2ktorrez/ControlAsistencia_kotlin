package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.Database

data class MAsistencia(
    var id: Int = 0,
    var fecha_hora_registro: String = "",
    var wifi_verificado: Boolean = false,
    var qr_verificado: Boolean = false,
    var id_clase: Int = 0,
    var id_estudiante: String = ""
) {
    fun insertar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("fecha_hora_registro", fecha_hora_registro)
            put("wifi_verificado", if (wifi_verificado) 1 else 0)
            put("qr_verificado", if (qr_verificado) 1 else 0)
            put("id_clase", id_clase)
            put("id_estudiante", id_estudiante)
        }
        val newRowId = db.insert(Database.Companion.TABLE_ASISTENCIA, null, values)
        db.close()
        return newRowId != -1L
    }

    companion object {
        fun obtener(context: Context, id: Int): MAsistencia? {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_ASISTENCIA,
                arrayOf("id", "fecha_hora_registro", "wifi_verificado", "qr_verificado", "id_clase", "id_estudiante"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var asistencia: MAsistencia? = null
            if (cursor.moveToFirst()) {
                asistencia = MAsistencia(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora_registro")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("wifi_verificado")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("qr_verificado")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_clase")),
                    cursor.getString(cursor.getColumnIndexOrThrow("id_estudiante"))
                )
            }
            cursor.close()
            db.close()
            return asistencia
        }

        fun listar(context: Context): List<MAsistencia> {
            val list = mutableListOf<MAsistencia>()
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_ASISTENCIA,
                arrayOf("id", "fecha_hora_registro", "wifi_verificado", "qr_verificado", "id_clase", "id_estudiante"),
                null, null, null, null, "id DESC"
            )
            if (cursor.moveToFirst()) {
                do {
                    val a = MAsistencia(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora_registro")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("wifi_verificado")) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow("qr_verificado")) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_clase")),
                        cursor.getString(cursor.getColumnIndexOrThrow("id_estudiante"))
                    )
                    list.add(a)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }

        fun obtenerClase(context: Context, idClase: Int): String {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_CLASE,
                arrayOf("fecha", "hora_inicio", "hora_fin"),
                "id = ?",
                arrayOf(idClase.toString()),
                null, null, null
            )
            var infoClase = "Clase no encontrada"
            if (cursor.moveToFirst()) {
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val horaInicio = cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio"))
                val horaFin = cursor.getString(cursor.getColumnIndexOrThrow("hora_fin"))
                infoClase = "$fecha $horaInicio-$horaFin"
            }
            cursor.close()
            db.close()
            return infoClase
        }

        fun obtenerEstudiante(context: Context, registro: String): String {
            val dbh = Database(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                Database.Companion.TABLE_ESTUDIANTE,
                arrayOf("nombre", "apellidop", "apellidom"),
                "registro = ?",
                arrayOf(registro),
                null, null, null
            )
            var nombreCompleto = "Estudiante no encontrado"
            if (cursor.moveToFirst()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellidop = cursor.getString(cursor.getColumnIndexOrThrow("apellidop"))
                val apellidom = cursor.getString(cursor.getColumnIndexOrThrow("apellidom"))
                nombreCompleto = "$nombre $apellidop $apellidom"
            }
            cursor.close()
            db.close()
            return nombreCompleto
        }
    }

    fun actualizar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("fecha_hora_registro", fecha_hora_registro)
            put("wifi_verificado", if (wifi_verificado) 1 else 0)
            put("qr_verificado", if (qr_verificado) 1 else 0)
            put("id_clase", id_clase)
            put("id_estudiante", id_estudiante)
        }
        val rows = db.update(Database.Companion.TABLE_ASISTENCIA, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    fun eliminar(context: Context): Boolean {
        val dbh = Database(context)
        val db = dbh.writableDatabase
        val rows = db.delete(Database.Companion.TABLE_ASISTENCIA, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}