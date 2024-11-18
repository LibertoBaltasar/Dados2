package com.example.dados2.pig

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dados2.databinding.ActivityFinalBinding

class ActivityFinal : AppCompatActivity() {
    private lateinit var listaJugadoresFinal: ArrayList<Jugador>
    private lateinit var binding: ActivityFinalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaJugadoresFinal = intent.getSerializableExtra("listaJugadoresFinal") as ArrayList<Jugador>

        val adapter = JugadorAdapterFinal(this, listaJugadoresFinal)
        binding.listaJugadores.adapter = adapter
    }
}