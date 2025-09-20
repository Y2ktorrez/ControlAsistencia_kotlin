package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VGrupoView

class AGrupo : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)

        VGrupoView(this)
    }
}