package com.example.controlasistencia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_simple)
        
        val btnMaterias: Button = findViewById(R.id.btnMaterias)
        val btnGrupos: Button = findViewById(R.id.btnGrupos)
        val btnAlumnos: Button = findViewById(R.id.btnAlumnos)
        val btnClases: Button = findViewById(R.id.btnClases)
        
        btnMaterias.setOnClickListener {
            startActivity(Intent(this, AMateria::class.java))
        }
        
        btnGrupos.setOnClickListener {
            startActivity(Intent(this, AGrupo::class.java))
        }

        btnAlumnos.setOnClickListener {
            startActivity(Intent(this, AAlumno::class.java))
        }

        btnClases.setOnClickListener {
            startActivity(Intent(this, AClase::class.java))
        }
    }
}