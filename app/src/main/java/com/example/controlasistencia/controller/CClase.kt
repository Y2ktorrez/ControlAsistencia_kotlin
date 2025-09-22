package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.view.VClaseView
import java.text.SimpleDateFormat
import java.util.*

class CClaseController(
    private val context: Context,
    private val view: VClaseView
) {
    
    fun cargarClases() {
        val clases = MClase.listar(context)
        view.mostrarClases(clases)
    }
    
    fun cargarGrupos() {
        val grupos = MGrupo.listar(context)
        view.mostrarGrupos(grupos)
    }
    
    fun mostrarEditar(clase: MClase) {
        cargarGrupos()
        view.mostrarFormularioEditar(clase)
    }
    
    fun crearClase(fecha: String, horaInicio: String, horaFin: String, idGrupo: Int) {
        val estado = determinarEstado(fecha, horaFin)
        val clase = MClase(
            fecha = fecha,
            hora_inicio = horaInicio,
            hora_fin = horaFin,
            estado = estado,
            id_grupo = idGrupo
        )
        clase.insertar(context)
        actualizarVista()
    }
    
    fun actualizarClase(clase: MClase, nuevaFecha: String, nuevaHoraInicio: String, nuevaHoraFin: String, nuevoIdGrupo: Int) {
        val nuevoEstado = determinarEstado(nuevaFecha, nuevaHoraFin)
        clase.fecha = nuevaFecha
        clase.hora_inicio = nuevaHoraInicio
        clase.hora_fin = nuevaHoraFin
        clase.estado = nuevoEstado
        clase.id_grupo = nuevoIdGrupo
        clase.actualizar(context)
        actualizarVista()
    }
    
    private fun determinarEstado(fecha: String, horaFin: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val fechaHoraFin = dateFormat.parse("$fecha $horaFin")
            val ahora = Date()
            
            if (fechaHoraFin != null && ahora.after(fechaHoraFin)) {
                "Finalizado"
            } else {
                "Activo"
            }
        } catch (e: Exception) {
            "Activo"
        }
    }
    
    fun eliminarClase(clase: MClase) {
        clase.eliminar(context)
        actualizarVista()
    }
    
    fun obtenerGrupo(idGrupo: Int): String {
        return MClase.obtenerGrupo(context, idGrupo)
    }

    fun obtenerMateria(idMateria: Int): String {
        return MGrupo.obtenerMateria(context, idMateria)
    }

    fun obtenerClase(idClase: Int): MClase? {
        return MClase.obtener(context, idClase)
    }
    
    fun actualizarVista() {
        cargarClases()
        cargarGrupos()
    }
}