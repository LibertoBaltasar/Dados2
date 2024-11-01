package com.example.dados2

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dados2.databinding.ActivityJuegoBinding
import kotlin.random.Random

class JuegoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJuegoBinding
    private var numJugadores: Int =2
    private var numRondas: Int =5
    val listaJugadores: MutableList<String> = mutableListOf()
    val juegoIntent= Intent(this, JuegoActivity::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)
        binding = ActivityJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        numJugadores=intent.getIntExtra("numJugadores",2)
        numRondas=intent.getIntExtra("numRondas",5)
        gestionarJugadores()
    }

    private fun gestionarJugadores(){
        listaJugadores.add(juegoIntent.getStringExtra("jugador1") ?:"J1")
        listaJugadores.add(juegoIntent.getStringExtra("jugador2") ?:"J2")
        if(numJugadores==3) {
            listaJugadores.add(juegoIntent.getStringExtra("jugador3") ?: "J3")
        }
        if(numJugadores==4) {
            listaJugadores.add(juegoIntent.getStringExtra("jugador4") ?:"J4")
        }
        randomizarOrdenJugadores()
    }
    private fun randomizarOrdenJugadores(){
        do {
            var contador:Int=1
            var num=generarNumAleatorio(1,listaJugadores.size)
            val cellId = resources.getIdentifier("nomJ${contador}", "id", packageName)
            val cellView = binding.casillero.findViewById<TextView>(cellId)
            cellView?.text = listaJugadores.get(num)
            listaJugadores.removeAt(num)
        }while(listaJugadores.isNotEmpty())
    }

//    private fun gestionarTirada(numJugadores:Int){
//        binding.Play.setOnClickListener {
//            val numero:Int = tirarDado()
//            seleccionarDado(numero)
//            if (numero==1){
//                Toast.makeText(this@JuegoActivity,"Lo siento, turno del siguiente", Toast.LENGTH_SHORT).show()
//                pasarTurno(numJugadores,true)
//            }else{
////                actualizarPuntuacionActualJugador(numero)
//                Toast.makeText(this@JuegoActivity,"Felicidades has anotado $numero", Toast.LENGTH_SHORT).show()
//                binding.pasar.visibility= View.VISIBLE
//            }
//        }
//        binding.pasar.setOnClickListener {
//            pasarTurno(numJugadores,false)
//        }
//    }
//
//    private fun pasarTurno(numJugadores: Int, fallo:Boolean){
//        val jugadorActual = binding.jugadorActual.text.toString().toInt()
//        val rondaActual = binding.rondaActual.text.toString().toInt()
//        if (fallo){
//            vaciarPuntuacionActual()
//        }
//        actualizarPuntuacionFinal()
//        //TODO:revisar el bucle
//        binding.pasar.visibility= View.INVISIBLE
//        if (rondaActual<5){
//            if (jugadorActual<numJugadores){
//                binding.jugadorActual.text =
//                    (jugadorActual + 1).toString()
//            }else{
//                binding.rondaActual.text =
//                    (rondaActual + 1).toString()
//                binding.jugadorActual.text = "1"
//            }
//        }else{
//            if (jugadorActual<numJugadores){
//                binding.jugadorActual.text =
//                    (jugadorActual + 1).toString()
//            }else{
//                finalizarPartida()
//            }
//        }
//    }
//
//    private fun finalizarPartida() {
//        Toast.makeText(this@JuegoActivity, "Has terminado la partida", Toast.LENGTH_SHORT).show()
//        binding.pasar.visibility= View.INVISIBLE
//        binding.Play.visibility = View.INVISIBLE
//        binding.indicadorEstado.visibility = View.INVISIBLE
//        var ganador=seleccionarGanador()
//        if (ganador.length==1) {
//            binding.mensajeVictoria.text = "Has ganado la partida jugador: ${seleccionarGanador()}"
//        }else{
//            binding.mensajeVictoria.text = "Han ganado la partida los jugadores: ${seleccionarGanador()}"
//        }
//        binding.mensajeVictoria.visibility= View.VISIBLE
//        binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dadovictoria))
////        binding.reset.visibility=View.VISIBLE
////        reiniciarPartida()
//    }
//
//    private fun seleccionarGanador(): String {
//        val puntuaciones = mutableListOf<Int>(
//            binding.j1Total.text.toString().toIntOrNull() ?: 0,
//            binding.j2Total.text.toString().toIntOrNull() ?: 0,
//            binding.j3Total.text.toString().toIntOrNull() ?: -1,
//            binding.j4Total.text.toString().toIntOrNull() ?: -1
//        )
//
//        var ganadores = verJugadorMaxPuntuacion(puntuaciones).toString()
//        var maxPuntuacion = puntuaciones[verJugadorMaxPuntuacion(puntuaciones)-1]
//        for (i in 1..<puntuaciones.size) {
//            if(puntuaciones[i] == maxPuntuacion){
//                ganadores += ", ${(i + 1)}"
//            }
//        }
//        return ganadores
//    }
//
//    private fun verJugadorMaxPuntuacion(puntuaciones: List<Int>): Int {
//        var ganador = 0
//        var maxPuntuacion = puntuaciones[0]
//
//        for (i in 1..3) {
//            if (puntuaciones[i] > maxPuntuacion) {
//                maxPuntuacion = puntuaciones[i]
//                ganador = i
//            }
//        }
//
//        return ganador + 1
//    }

    private fun seleccionarDado(opcion: Int) {
        when (opcion) {
            1 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado1))
            2 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado2))
            3 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado3))
            4 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado4))
            5 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado5))
            6 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado6))
        }
    }

    private fun tirarDado(): Int {
        return generarNumAleatorio(1,7)
    }
    private fun generarNumAleatorio(inicio: Int,fin:Int): Int {
        return Random.nextInt(inicio, fin)
    }

}