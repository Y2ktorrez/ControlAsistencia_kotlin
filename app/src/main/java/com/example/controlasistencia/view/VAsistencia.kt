package com.example.controlasistencia.view

import android.content.Context
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CAsistencia
import com.example.controlasistencia.model.MAsistencia
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MAlumno

class VAsistencia(private val context: Context) {

    private val controller = CAsistencia(context)
    
    // Componentes de la vista según el diagrama
    lateinit var txtWifi: TextView
    lateinit var imgQR: ImageView
    lateinit var btnMostrarQR: Button
    lateinit var spinnerClase: Spinner
    lateinit var spinnerEstudiante: Spinner
    lateinit var btnRegistrarAsistencia: Button
    lateinit var btnListarAsistencias: Button

    // Datos para los spinners
    private var clasesActivas = listOf<MClase>()
    private var estudiantes = listOf<MAlumno>()

    // inicializar(activity): void
    fun inicializar(activity: android.app.Activity) {
        txtWifi = activity.findViewById(R.id.txtWifi)
        imgQR = activity.findViewById(R.id.imgQR)
        btnMostrarQR = activity.findViewById(R.id.btnMostrarQR)
        spinnerClase = activity.findViewById(R.id.spinnerClase)
        spinnerEstudiante = activity.findViewById(R.id.spinnerEstudiante)
        btnRegistrarAsistencia = activity.findViewById(R.id.btnRegistrarAsistencia)
        btnListarAsistencias = activity.findViewById(R.id.btnListarAsistencias)

        configurarEventos()
        verificarWifi()
        cargarClasesActivas()
    }

    // configurarEventos(): void
    private fun configurarEventos() {
        btnMostrarQR.setOnClickListener {
            mostrarQR()
        }

        btnRegistrarAsistencia.setOnClickListener {
            registrarAsistencia()
        }

        btnListarAsistencias.setOnClickListener {
            listarAsistencias()
        }

        spinnerClase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (clasesActivas.isNotEmpty() && position < clasesActivas.size) {
                    cargarEstudiantesGrupo(clasesActivas[position].id_grupo)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // verificarWifi(): void
    private fun verificarWifi() {
        val wifiVerificado = controller.verificarWifi()
        txtWifi.text = if (wifiVerificado) "WiFi: Verificado ✓" else "WiFi: No verificado ✗"
        txtWifi.setTextColor(
            if (wifiVerificado) 
                android.graphics.Color.parseColor("#4CAF50") 
            else 
                android.graphics.Color.parseColor("#F44336")
        )
    }

    // cargarClasesActivas(): void
    private fun cargarClasesActivas() {
        clasesActivas = controller.obtenerClasesActivas()
        val opcionesClase = clasesActivas.map { clase ->
            val infoGrupo = controller.obtenerClase(clase.id)
            "Clase ${clase.id} - ${clase.fecha} ${clase.hora_inicio}"
        }

        if (opcionesClase.isEmpty()) {
            Toast.makeText(context, "No hay clases activas en este momento", Toast.LENGTH_LONG).show()
            spinnerClase.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf("No hay clases activas"))
        } else {
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, opcionesClase)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerClase.adapter = adapter
        }
    }

    // cargarEstudiantesGrupo(idGrupo: int): void
    private fun cargarEstudiantesGrupo(idGrupo: Int) {
        estudiantes = controller.obtenerEstudiantesGrupo(idGrupo)
        val opcionesEstudiante = estudiantes.map { estudiante ->
            "${estudiante.registro} - ${estudiante.nombre} ${estudiante.apellidop}"
        }

        if (opcionesEstudiante.isEmpty()) {
            Toast.makeText(context, "No hay estudiantes en este grupo", Toast.LENGTH_SHORT).show()
            spinnerEstudiante.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listOf("No hay estudiantes"))
        } else {
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, opcionesEstudiante)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstudiante.adapter = adapter
        }
    }

    // mostrarQR(): void - Implementación del método del diagrama
    private fun mostrarQR() {
        if (clasesActivas.isEmpty()) {
            Toast.makeText(context, "No hay clases activas para generar QR", Toast.LENGTH_SHORT).show()
            return
        }

        val posicionClase = spinnerClase.selectedItemPosition
        if (posicionClase >= 0 && posicionClase < clasesActivas.size) {
            val claseSeleccionada = clasesActivas[posicionClase]
            val datosQR = "CLASE:${claseSeleccionada.id}|FECHA:${claseSeleccionada.fecha}|HORA:${claseSeleccionada.hora_inicio}"
            
            val qrBitmap = controller.generarQR(datosQR)
            if (qrBitmap != null) {
                imgQR.setImageBitmap(qrBitmap)
                imgQR.visibility = android.view.View.VISIBLE
                Toast.makeText(context, "Código QR generado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al generar código QR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // registrarAsistencia(): void
    private fun registrarAsistencia() {
        if (clasesActivas.isEmpty() || estudiantes.isEmpty()) {
            Toast.makeText(context, "Seleccione una clase válida y un estudiante", Toast.LENGTH_SHORT).show()
            return
        }

        val posicionClase = spinnerClase.selectedItemPosition
        val posicionEstudiante = spinnerEstudiante.selectedItemPosition

        if (posicionClase >= 0 && posicionClase < clasesActivas.size &&
            posicionEstudiante >= 0 && posicionEstudiante < estudiantes.size) {
            
            val claseSeleccionada = clasesActivas[posicionClase]
            val estudianteSeleccionado = estudiantes[posicionEstudiante]

            // Verificar si ya existe asistencia
            if (controller.verificarAsistenciaExistente(claseSeleccionada.id, estudianteSeleccionado.registro)) {
                Toast.makeText(context, "Ya existe asistencia registrada para este estudiante en esta clase", Toast.LENGTH_LONG).show()
                return
            }

            val asistencia = MAsistencia(
                id_clase = claseSeleccionada.id,
                id_estudiante = estudianteSeleccionado.registro,
                wifi_verificado = controller.verificarWifi(),
                qr_verificado = true // Se asume QR verificado al registrar manualmente
            )

            if (controller.guardarAsistencia(asistencia)) {
                Toast.makeText(context, "Asistencia registrada exitosamente", Toast.LENGTH_SHORT).show()
                limpiarFormulario()
            } else {
                Toast.makeText(context, "Error al registrar asistencia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // listarAsistencias(): void
    private fun listarAsistencias() {
        val asistencias = controller.listarAsistencias()
        if (asistencias.isEmpty()) {
            Toast.makeText(context, "No hay asistencias registradas", Toast.LENGTH_SHORT).show()
            return
        }

        val listaTexto = StringBuilder()
        listaTexto.append("ASISTENCIAS REGISTRADAS:\n\n")

        for (asistencia in asistencias) {
            val infoClase = controller.obtenerClase(asistencia.id_clase)
            val infoEstudiante = controller.obtenerEstudiante(asistencia.id_estudiante)
            
            listaTexto.append("ID: ${asistencia.id}\n")
            listaTexto.append("Estudiante: $infoEstudiante\n")
            listaTexto.append("Clase: $infoClase\n")
            listaTexto.append("Fecha/Hora: ${asistencia.fecha_hora_registro}\n")
            listaTexto.append("WiFi: ${if (asistencia.wifi_verificado) "✓" else "✗"}\n")
            listaTexto.append("QR: ${if (asistencia.qr_verificado) "✓" else "✗"}\n")
            listaTexto.append("------------------------\n")
        }

        mostrarDialogo("Lista de Asistencias", listaTexto.toString())
    }

    // limpiarFormulario(): void
    private fun limpiarFormulario() {
        imgQR.visibility = android.view.View.GONE
        cargarClasesActivas()
        verificarWifi()
    }

    // mostrarDialogo(titulo: String, mensaje: String): void
    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}