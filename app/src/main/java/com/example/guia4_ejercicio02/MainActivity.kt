package com.example.guia4_ejercicio02

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btnIniciarGrabacion: Button
    private lateinit var tvEstado: TextView

    private val solicitarPermisoAudio = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permisoConcedido ->
        if (permisoConcedido) {
            mostrarBotonDisponible()
        } else {
            mostrarPermisoDenegado()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnIniciarGrabacion = findViewById(R.id.btnIniciarGrabacion)
        tvEstado = findViewById(R.id.tvEstado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnIniciarGrabacion.setOnClickListener {
            if (permisoAudioConcedido()) {
                tvEstado.text = "Grabando"
            } else {
                tvEstado.text = "Esperando permiso"
                solicitarPermisoAudio.launch(Manifest.permission.RECORD_AUDIO)
            }
        }

        comprobarPermisoInicial()
    }

    private fun comprobarPermisoInicial() {
        if (permisoAudioConcedido()) {
            mostrarBotonDisponible()
        } else {
            btnIniciarGrabacion.isEnabled = false
            tvEstado.text = "Esperando permiso"
            solicitarPermisoAudio.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun permisoAudioConcedido(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun mostrarBotonDisponible() {
        btnIniciarGrabacion.isEnabled = true
        tvEstado.text = "Esperando permiso"
    }

    private fun mostrarPermisoDenegado() {
        btnIniciarGrabacion.isEnabled = false
        tvEstado.text = "Permiso denegado"
    }
}
