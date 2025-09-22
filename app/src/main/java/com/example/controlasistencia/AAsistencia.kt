package com.example.controlasistencia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.controlasistencia.view.VAsistencia

class AAsistencia : AppCompatActivity() {

    private lateinit var vista: VAsistencia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)

        vista = VAsistencia(this)
        vista.inicializar(this)
    }
}