package com.example.controlasistencia.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.example.controlasistencia.model.MAsistencia
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MAlumno
import java.text.SimpleDateFormat
import java.util.*

class CAsistencia(private val context: Context) {

    fun obtenerClase(idClase: Int): String {
        return MAsistencia.obtenerClase(context, idClase)
    }

    fun obtenerEstudiante(registro: String): String {
        return MAsistencia.obtenerEstudiante(context, registro)
    }

    fun generarQR(datos: String): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(datos, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    fun guardarAsistencia(asistencia: MAsistencia): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        asistencia.fecha_hora_registro = sdf.format(Date())
        
        return asistencia.insertar(context)
    }

    fun verificarWifi(): Boolean {
        return true
    }

    fun obtenerClasesActivas(): List<MClase> {
        val todasLasClases = MClase.listar(context)
        val clasesActivas = mutableListOf<MClase>()
        
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val ahora = Date()
        
        for (clase in todasLasClases) {
            try {
                val fechaHoraInicio = sdf.parse("${clase.fecha} ${clase.hora_inicio}")
                val fechaHoraFin = sdf.parse("${clase.fecha} ${clase.hora_fin}")
                
                if (fechaHoraInicio != null && fechaHoraFin != null) {
                    if (ahora.after(fechaHoraInicio) && ahora.before(fechaHoraFin)) {
                        clasesActivas.add(clase)
                    }
                }
            } catch (e: Exception) {
            }
        }
        
        return clasesActivas
    }

    fun obtenerEstudiantesGrupo(idGrupo: Int): List<MAlumno> {
        return MAlumno.listar(context)
    }

    fun verificarAsistenciaExistente(idClase: Int, registro: String): Boolean {
        val asistencias = MAsistencia.listar(context)
        return asistencias.any { it.id_clase == idClase && it.id_estudiante == registro }
    }

    fun actualizarAsistencia(asistencia: MAsistencia): Boolean {
        return asistencia.actualizar(context)
    }

    fun listarAsistencias(): List<MAsistencia> {
        return MAsistencia.listar(context)
    }

    fun eliminarAsistencia(id: Int): Boolean {
        val asistencia = MAsistencia.obtener(context, id)
        return asistencia?.eliminar(context) ?: false
    }
}