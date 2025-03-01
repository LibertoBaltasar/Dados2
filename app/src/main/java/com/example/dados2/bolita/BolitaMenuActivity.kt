package com.example.dados2.bolita

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.startActivity
import com.example.dados2.basedatos.AppDatabase
import com.example.dados2.basedatos.Jugador
import com.example.dados2.bolita.ui.theme.Dados2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BolitaMenuActivity : ComponentActivity() {
    private lateinit var jugador: String
    private var puntajeMaximo: Int = 0
    private var puntajeAcumulado: Int = 0
    private var destructor: Boolean = false
    private var imparable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        jugador = intent.getStringExtra("nombre").toString() // Inicializar jugador aquí
        try {
            cargarDatos()
        } catch (e: Exception) {
            puntajeMaximo = 0
            puntajeAcumulado = 0
        }
        setContent {
            Dados2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    bolitaMenuScreen(jugador, puntajeMaximo, puntajeAcumulado, destructor,imparable, this) {
                        insertarJugador(jugador, puntajeMaximo, puntajeAcumulado)
                    }
                }
            }
        }
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val nombreJugador = intent.getStringExtra("nombre") ?: return@launch
            val jugador = AppDatabase.getInstance(this@BolitaMenuActivity).jugadorDao().findByName(nombreJugador)
            jugador?.let {
                puntajeMaximo = it.puntajeMax
                puntajeAcumulado = it.puntajeAcumulado
                destructor = it.destructor
                imparable = it.imparable
            }
        }
    }

    private fun insertarJugador(jugador: String, puntajeMaximo: Int, puntajeAcumulado: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val jugadorExistente = AppDatabase.getInstance(this@BolitaMenuActivity).jugadorDao().findByName(jugador)
            if (jugadorExistente == null) {
                AppDatabase.getInstance(this@BolitaMenuActivity).jugadorDao().insertJugador(
                    jugador = Jugador(nombre = jugador, puntajeMax = puntajeMaximo, puntajeAcumulado = puntajeAcumulado, destructor = false, imparable = false)
                )
            }
        }
    }
}

@Composable
fun bolitaMenuScreen(
    jugador: String,
    puntajeMaximo: Int,
    puntajeAcumulado: Int,
    destructor:Boolean,
    imparable:Boolean,
    context: Context,
    onPlayButtonClick: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jugador: $jugador")
        Text(text = "Puntaje máximo: $puntajeMaximo")
        Text(text = "Puntaje acumulado: $puntajeAcumulado")
        if (imparable) {
            Text(text = "¡Eres imparable!")
        }
        if (destructor) {
            Text(text = "¡Eres destructor!")
        }
        Button(onClick = {
            onPlayButtonClick()
            val intent = Intent(context, BolitaJuegoActivity::class.java)
            intent.putExtra("nombre", jugador)
            startActivity(context, intent, null)
        }) {
            Text(text = "Jugar")
        }
    }
}