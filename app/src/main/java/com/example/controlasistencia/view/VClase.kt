package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CClaseController
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MGrupo

class VClaseView(private val activity: Activity) {
    
    private val controller = CClaseController(activity, this)
    private val txtFecha: EditText = activity.findViewById(R.id.txtFecha)
    private val txtHoraInicio: EditText = activity.findViewById(R.id.txtHora_Inicio)
    private val txtHoraFin: EditText = activity.findViewById(R.id.txtHora_Fin)
    private val selecGrupo: Spinner = activity.findViewById(R.id.selecGrupo)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminar)
    private val lstClases: ListView = activity.findViewById(R.id.lstClases)
    
    private var claseEditando: MClase? = null
    private var adapter: ArrayAdapter<String>? = null
    private var grupos: List<MGrupo> = emptyList()
    
    init {
        controller.actualizarVista()
        setupListeners()
    }
    
    private fun setupListeners() {
        btnGuardar.setOnClickListener { guardarClase() }
        btnEliminar.setOnClickListener { eliminarClase() }
        
        lstClases.setOnItemClickListener { _, _, position, _ ->
            val claseTexto = adapter?.getItem(position) ?: ""
            val id = claseTexto.split(" - ")[0].toInt()
            val clase = controller.obtenerClase(id)
            clase?.let { controller.mostrarEditar(it) }
        }
    }
    
    fun mostrarClases(clases: List<MClase>) {
        val items = clases.map { "${it.id} - ${it.fecha} ${it.hora_inicio}-${it.hora_fin} (${controller.obtenerGrupo(it.id_grupo)}) - ${it.estado}" }
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lstClases.adapter = adapter
    }
    
    fun mostrarGrupos(grupos: List<MGrupo>) {
        this.grupos = grupos
        val items = grupos.map { "${it.nombre} - ${controller.obtenerMateria(it.id_materia)}" }
        val spinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selecGrupo.adapter = spinnerAdapter
    }
    
    fun mostrarFormularioCrear() {
        txtFecha.setText("")
        txtHoraInicio.setText("")
        txtHoraFin.setText("")
        selecGrupo.setSelection(0)
        claseEditando = null
        btnEliminar.isEnabled = false
    }
    
    fun mostrarFormularioEditar(clase: MClase) {
        txtFecha.setText(clase.fecha)
        txtHoraInicio.setText(clase.hora_inicio)
        txtHoraFin.setText(clase.hora_fin)
        val grupoIndex = grupos.indexOfFirst { it.id == clase.id_grupo }
        if (grupoIndex >= 0) selecGrupo.setSelection(grupoIndex)
        claseEditando = clase
        btnEliminar.isEnabled = true
    }
    
    private fun guardarClase() {
        val fecha = txtFecha.text.toString()
        val horaInicio = txtHoraInicio.text.toString()
        val horaFin = txtHoraFin.text.toString()
        val grupoSeleccionado = grupos[selecGrupo.selectedItemPosition]
        
        if (claseEditando != null) {
            controller.actualizarClase(claseEditando!!, fecha, horaInicio, horaFin, grupoSeleccionado.id)
        } else {
            controller.crearClase(fecha, horaInicio, horaFin, grupoSeleccionado.id)
        }
        limpiarFormulario()
    }
    
    private fun eliminarClase() {
        claseEditando?.let {
            controller.eliminarClase(it)
            limpiarFormulario()
        }
    }
    
    private fun limpiarFormulario() {
        txtFecha.setText("")
        txtHoraInicio.setText("")
        txtHoraFin.setText("")
        selecGrupo.setSelection(0)
        claseEditando = null
        btnEliminar.isEnabled = false
    }
}